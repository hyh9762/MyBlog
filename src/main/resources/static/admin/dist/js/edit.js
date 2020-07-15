var blogEditor;
// Tags Input
/*$('#tags').tagsInput({
    width: '100%',
    height: '38px',
    defaultText: '文章标签'
});*/


//Initialize Select2 Elements
$('.select2').select2()

$(function () {
    blogEditor = editormd("blog-editormd", {
        width: "100%",
        height: 640,
        syncScrolling: "single",
        path: "/admin/plugins/editormd/lib/",
        toolbarModes: 'full',
        /**图片上传配置*/
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"], //图片上传格式
        imageUploadURL: "/admin/blogs/md/uploadfile",
        onload: function (obj) { //上传成功之后的回调
        }
    });

    /* // 编辑器粘贴上传
     document.getElementById("blog-editormd").addEventListener("paste", function (e) {
         var clipboardData = e.clipboardData;
         if (clipboardData) {
             var items = clipboardData.items;
             if (items && items.length > 0) {
                 for (var item of items) {
                     if (item.type.startsWith("image/")) {
                         var file = item.getAsFile();
                         if (!file) {
                             alert("请上传有效文件");
                             return;
                         }
                         var formData = new FormData();
                         formData.append('file', file);
                         var xhr = new XMLHttpRequest();
                         xhr.open("POST", "/admin/upload/file");
                         xhr.onreadystatechange = function () {
                             if (xhr.readyState == 4 && xhr.status == 200) {
                                 var json = JSON.parse(xhr.responseText);
                                 if (json.resultCode == 200) {
                                     blogEditor.insertValue("![](" + json.data + ")");
                                 } else {
                                     alert("上传失败");
                                 }
                             }
                         }
                         xhr.send(formData);
                     }
                 }
             }
         }
     });*/

    new AjaxUpload('#uploadFirstPicture', {
        action: '/admin/upload/file',
        name: 'file',
        autoSubmit: true,
        //responseType: "json",
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                alert('只支持jpg、png、gif格式的文件！');
                return false;
            }
        },
        onComplete: function (file, r) {
            if (r != null) {
                $("#firstPicture").attr("src", r)
                    .attr("style", "width: 128px;height: 128px;display:block;");
                return false;
            } else {
                alert("error");
            }
        }
    });
});

$('#confirmButton').click(function () {
    var title = $('#title').val();
    var subUrl = $('#subUrl').val();
    var typeId = $('#types').val();
    var tags = $('#tags').val();
    var content = blogEditor.getMarkdown();
    var description = $('#description').val();
    if (isNull(title)) {
        swal("请输入文章标题", {
            icon: "error",
        });
        return;
    }
    if (!validLength(title, 150)) {
        swal("标题过长", {
            icon: "error",
        });
        return;
    }
    if (!validLength(subUrl, 150)) {
        swal("路径过长", {
            icon: "error",
        });
        return;
    }
    if (isNull(typeId)) {
        swal("请选择文章分类", {
            icon: "error",
        });
        return;
    }
    if (isNull(tags)) {
        swal("请输入文章标签", {
            icon: "error",
        });
        return;
    }
    if (!validLength(tags, 150)) {
        swal("标签过长", {
            icon: "error",
        });
        return;
    }
    if (isNull(description)) {
        swal("请输入博客简介", {
            icon: "error",
        });
        return;
    }
    if (isNull(content)) {
        swal("请输入文章内容", {
            icon: "error",
        });
        return;
    }
    if (!validLength(content, 100000)) {
        swal("文章内容过长", {
            icon: "error",
        });
        return;
    }
    $('#articleModal').modal('show');
});

$('#saveButton').click(function () {
    var id = $('#id').val();
    var title = $('#title').val();
    var subUrl = $('#subUrl').val();
    var typeId = $('#types').val();
    var tags = $('#tags').val();
    var description = $('#description').val();
    var content = blogEditor.getMarkdown();
    var firstPicture = $('#firstPicture')[0].src;
    var flag = $("input[name='flag']:checked").val();
    var published = $("input[name='published']:checked").val();
    var commentabled = $("input[name='commentabled']:checked").val();
    var recommend = $("input[name='recommend']:checked").val();
    var shareStatement = $("input[name='shareStatement']:checked").val();
    var appreciation = $("input[name='appreciation']:checked").val();
    if (isNull(firstPicture) || firstPicture.indexOf('img-upload') != -1) {
        swal("封面图片不能为空", {
            icon: "error",
        });
        return;
    }
    var url = '/admin/blogs/save';
    var swlMessage = '保存成功';
    var type = {
        "id": typeId
    };
    var data = {
        "title": title, "subUrl": subUrl, "type": type,
        "tagIds": tags, "content": content, "firstPicture": firstPicture, "published": published,
        "description": description, "flag": flag, "commentabled": commentabled,
        "recommend": recommend, "shareStatement": shareStatement, "appreciation": appreciation
    };
    if (id > 0) {
        url = '/admin/blogs/update';
        swlMessage = '修改成功';
        data = {
            "id": id,
            "title": title, "subUrl": subUrl, "type": type,
            "tagIds": tags, "content": content, "firstPicture": firstPicture, "published": published,
            "description": description, "flag": flag, "commentabled": commentabled,
            "recommend": recommend, "shareStatement": shareStatement, "appreciation": appreciation
        };
    }
    console.log(data);
    $.ajax({
        type: 'POST',//方法类型
        contentType: "application/json;charset=UTF-8",
        url: url,
        data: JSON.stringify(data),
        success: function (result) {
            if (result) {
                $('#articleModal').modal('hide');
                swal({
                    title: swlMessage,
                    type: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '返回博客列表',
                    confirmButtonClass: 'btn btn-success',
                    buttonsStyling: false
                }).then(function () {
                    window.location.href = "/admin/blogs";
                })
            } else {
                $('#articleModal').modal('hide');
                swal("保存失败", {
                    icon: "error",
                });
            }
            ;
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});

$('#cancelButton').click(function () {
    window.location.href = "/admin/blogs";
});

/**
 * 随机封面功能
 */
$('#randomFirstPicture').click(function () {
    var rand = parseInt(Math.random() * 40 + 1);
    $("#firstPicture").attr("src", '/admin/dist/img/rand/' + rand + ".jpg")
        .attr("style", "width:160px ;height: 120px;display:block;");
});
