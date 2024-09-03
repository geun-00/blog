import {handleIsLiked, handleLike} from "./like.js";
import {setupCommentSubmit} from "./comment.js";

document.addEventListener('DOMContentLoaded', function() {
    handleIsLiked();
    setupCommentSubmit();

    const likeIcon = document.getElementById('like-icon');
    likeIcon.addEventListener('click', handleLike)
})