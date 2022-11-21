
//window.addEventListener("load", function () {

   const canvas = document.querySelector("#canvas");
   const canvas1 = document.querySelector("#canvas1");
    const btn = document.querySelector("button");
   const ctx = canvas.getContext("2d");
   const ctx1 = canvas1.getContext("2d");
   let painting = false;

    const getBase64StringFromDataURL = (dataURL) =>
        dataURL.replace('data:', '').replace(/^.+,/, '');

    function startPosition(e) {
        painting = true;
        draw(e);
    }

    function finishedPosition() {
        let image = new Image();
        let strDataUri;

        ctx.beginPath();
        ctx1.beginPath();
        painting = false;

        strDataUri = canvas.toDataURL('image/base64');
        image.src = strDataUri;
        image.onload = function(){
            ctx1.drawImage(image,0,0,100,100,0,0,28,28);
        };
        ctx1.stroke();
    }

    function draw(e) {
        if (!painting)
            return
        else {
            ctx.lineWidth = 5;
            ctx.lineCap = "round";
            ctx.lineTo(e.clientX, e.clientY);
            ctx.stroke();
            ctx.beginPath();
            ctx.moveTo(e.clientX, e.clientY);
        }
    }

    function sendRequest() {

        $(document).ready(function () {

            let url = 'http://localhost:8080/api/postImage';
            let strDataUri1;

            strDataUri1 = canvas1.toDataURL('image/base64');

            const base64 = getBase64StringFromDataURL(strDataUri1);

            console.log(base64);

            $.ajax({
                url: url,
                type: "POST",
                data: base64,
                //dataType: JSON,
                success: function (result) {
                    console.log(result);
                    printResult(result);
                },
                error: function (error) {
                    console.log(error)
                }
            })
        })

    }

    function printResult(result) {
        p.textContent = result
    }

    // Event Listeners.
    canvas.addEventListener("mousedown", startPosition);
    canvas.addEventListener("mouseup", finishedPosition);
    canvas.addEventListener("mousemove", draw);
    btn.addEventListener("click", function () {
        sendRequest();
    });

//});


