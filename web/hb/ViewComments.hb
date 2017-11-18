<div class="panel panel-default" id="ViewComments">

    <td><button class="ViewComments-addComment" data-value="{{this.mMessageId}}">Add a comment</button></td>

    <table class="table">
        <tbody>
            {{#each mCommentData}}
            <tr>
                <td>{{this.mComment}}</td>

                <td><button class="ViewComments-viewProfile" data-value="{{this.mUsername}}">{{this.mUsername}}</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
</div>
