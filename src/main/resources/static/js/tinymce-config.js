tinymce.init({
    selector: "textarea#content",
    menubar: false,
    statusbar: false,
    plugins: ["image", "code", "autoresize"],
    toolbar: "undo redo | styleselect | fontselect | fontsizeselect | bold italic | alignleft aligncenter alignright alignjustify | outdent indent | image | code",
    images_upload_url: "/articles/upload-image",
    images_upload_handler: imageUploadHandler,
    image_dimensions: false
});


function imageUploadHandler(blobInfo, success, failure, progress) {

    var xhr, formData;

    xhr = new XMLHttpRequest();
    xhr.withCredentials = false;

    const url = "/articles/" + id + "/upload/image";

    xhr.open("POST", url);

    xhr.upload.onprogress = function (e) {
        progress(e.loaded / e.total * 100);
    };

    xhr.onload = function () {
        if (xhr.status === 403) {
            failure("HTTP Error: " + xhr.status, { remove: true });
            return;
        }

        if (xhr.status < 200 || xhr.status >= 300) {
            failure("HTTP Error: " + xhr.status);
            return;
        }

        console.log(`xhr.responseText: ${xhr.responseText}`);
        var json = JSON.parse(xhr.responseText);

        if (!json || typeof json.location != "string") {
            failure("Invalid JSON: " + xhr.responseText);
            return;
        }

        success(json.location);
    };

    xhr.onerror = function () {
        failure("Image upload failed due to a XHR Transport error. Code: " + xhr.status);
    };

    formData = new FormData();
    formData.append("file", blobInfo.blob(), blobInfo.filename());

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    xhr.setRequestHeader(header, token);
    xhr.send(formData);
};

