<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="header.jsp" />

<html>
<head>
    <title>Create a Quiz</title>
    <link rel="stylesheet" href="css/createQuiz.css" />
    <link rel="icon" href="images/BRAINBUZZ.png">

</head>
<body>
<div class="create-quiz-container">
    <h1>Create a New Quiz</h1>

    <form action="CreateQuizServlet" method="post">

        <label for="title">Quiz Title:</label><br>
        <input type="text" id="title" name="title" required><br><br>

        <label for="description">Description:</label><br>
        <textarea id="description" name="description" rows="4" cols="50"></textarea><br><br>

        <label for="category">Category:</label><br>
        <input type="text" id="category" name="category" required><br><br>

        <label for="tags">Tags (comma-separated, optional):</label><br>
        <input type="text" id="tags" name="tags" placeholder="e.g. presidents, US, Cold War"><br><br>

        <fieldset>
            <legend><strong>Quiz Options</strong></legend>

            <input type="checkbox" id="randomOrder" name="randomOrder">
            <label for="randomOrder">Randomize question order</label><br><br>

            <label><strong>Question Display Mode:</strong></label><br>
            <input type="radio" id="onePage" name="pageMode" value="onePage" checked>
            <label for="onePage">One Page (all questions shown at once)</label><br>
            <input type="radio" id="multiPage" name="pageMode" value="multiPage">
            <label for="multiPage">Multiple Pages (one question at a time)</label><br><br>

            <input type="checkbox" id="immediateCorrection" name="immediateCorrection">
            <label for="immediateCorrection">Enable immediate correction</label><br><br>

        </fieldset><br>

        <button type="submit">Create Quiz</button>
    </form>
</div>

</body>
</html>