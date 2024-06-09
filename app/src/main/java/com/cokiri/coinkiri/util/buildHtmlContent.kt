package com.cokiri.coinkiri.util

fun buildHtmlContent(content: String): String {
    val quillCssCdn = "https://cdn.jsdelivr.net/npm/quill@2.0.2/dist/quill.snow.css"
    val quillJsCdn = "https://cdn.jsdelivr.net/npm/quill@2.0.2/dist/quill.js"
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <link href="$quillCssCdn" rel="stylesheet">
            <style>
                body {
                    background-color: #F8F8F8;
                    margin: 0;
                    padding: 0;
                    display: flex;
                    flex-direction: column;
                    height: 100vh;
                    box-sizing: border-box;
                }
                #editor-container {
                    flex: 1;
                }
                #editor {
                    width: 100%;
                    height: 100%;
                    background-color: #FFFFFF;
                }
            </style>
        </head>
        <body>
            <div id="editor-container">
                <div id="editor">$content</div>
            </div>
            <script src="$quillJsCdn"></script>
            <script>
                var quill = new Quill('#editor', {
                    theme: 'snow',
                    readOnly: true,
                    modules: {
                        toolbar: false
                    }
                });
            </script>
        </body>
        </html>
    """.trimIndent()
}
