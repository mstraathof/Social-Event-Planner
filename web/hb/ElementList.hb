<div class="panel panel-default" id="ElementList">
    <h4 id="yours">Your Buzzes</h4>
    <!--
    <table class="table">
        <tbody>
            <!--
            <tr> 
                <th>Author</th>
                <th>Title</th>
                <th>Message</th>
                <th></th>
                <th>Time Created</th>
                <th>Vote Count</th>
                <th> </th>
                <th> </th>
            </tr>
            - ->
            
            {{#each mMessageData}}
            <tr>
                <!-- This element has info on all the values in mData - ->
                <td><button class="ElementList-profile" data-value="{{this.mUsername}}">{{this.mUsername}}</button></td>

                <td class ="ElementList-editbtn"> {{this.mSubject}}</td>
                <td class="col-md-6">{{this.mMessage}}</td>
                
                <td><button class="ElementList-comments" data-value="{{this.mId}}">View Comments</button></td>

                <td>{{this.mCreateTime}}</td>
                <td>{{this.mVotes}}</td>

                <!-- Data For both Edit and Delete Buttons on each Entry
                <td><button class="ElementList-editbtn" data-value="{{this.mId}}"data-message="{{this.mMessage}}"data-subject="{{this.mSubject}}">Edit</button></td>
                <td><button class="ElementList-comments" data-value="{{this.mId}}">Comments</button></td>
                <td><button class="ElementList-profile" data-value="{{this.mUsername}}">{{this.mUsername}}</button></td>
                - ->

                <td><button class="ElementList-upvote" data-value="{{this.mId}}">Upvote</button></td>
                <td><button class="ElementList-downvote" data-value="{{this.mId}}">Downvote</button></td>
            </tr>
            {{/each}}

        </tbody>
    </table>
    -->

    {{#each mMessageData}}
    <div>       <!-- style="background-color:blue;color:white;padding:20px;" -->
    <!-- <p><b><button class="ElementList-profile" data-value="{{this.mUsername}}">{{this.mUsername}}</button></b></p> -->
    <p><b><button class="ElementList-profile" data-value="{{this.mUsername}}">{{this.mUsername}}</button></b></p>
    <!-- <img src="https://thedomains.com/wp-content/buzz-bee-name-001-150x150.png" alt="Buzzing Bee"/> -->
    <img src={{"this.mWebUrl"}} alt="image" height=300/>
    <p class="ElementList-editbtn"><b>{{this.mSubject}}</b></p>
    <p class="col-md-6">{{this.mMessage}}</p>
    <p><a href="{{this.mUrl}}">{{this.mUrl}}</a></p>
    <p>Votes: {{this.mVotes}}   <button class="ElementList-upvote" data-value="{{this.mId}}">Upvote</button>  <button class="ElementList-downvote" data-value="{{this.mId}}">Downvote</button></p>
    <!-- <p style="text-indent:30px;"><button class="ElementList-comments" data-value="{{this.mId}}">View Comments</button></p> -->
    <p style="text-indent:30px;"><button class="ViewComments-addComment" data-value="{{this.mMessageId}}">Add a comment</button></p>
    <div style="text-indent:30px;"> 
        {{#each mCommentData}}
        <p>{{this.mComment}}</p>
    </div>
    <hr>
    <hr>
    </div>
    {{/each}}

    <h4 id="liked">Your Upvoted Buzzes</h4>
    <table class="table">
        <tbody>
            {{#each mLikedData}}
            <tr>
                <!-- This element has info on all the values in mData -->
                <td class ="ElementList-editbtn"> {{this.mSubject}}</td>
                <td class="col-md-6">{{this.mMessage}}</td>
                
                <td>{{this.mCreateTime}}</td>
                <td>{{this.mVotes}}</td>

                <td><button class="ElementList-comments" data-value="{{this.mId}}">Comments</button></td>
                <td><button class="ElementList-profile" data-value="{{this.mUsername}}">{{this.mUsername}}</button></td>
                <td><button class="ElementList-upvote" data-value="{{this.mId}}">Upvote</button></td>
                <td><button class="ElementList-downvote" data-value="{{this.mId}}">Downvote</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>

    <h4 id="disliked">Your Downvoted Buzzes</h4>
    <table class="table">
        <tbody>
            {{#each mDislikedData}}
            <tr>
                <!-- This element has info on all the values in mData -->
                <td class ="ElementList-editbtn"> {{this.mSubject}}</td>
                <td class="col-md-6">{{this.mMessage}}</td>
                
                <td>{{this.mCreateTime}}</td>
                <td>{{this.mVotes}}</td>

                <td><button class="ElementList-comments" data-value="{{this.mId}}">Comments</button></td>
                <td><button class="ElementList-profile" data-value="{{this.mUsername}}">{{this.mUsername}}</button></td>
                <td><button class="ElementList-upvote" data-value="{{this.mId}}">Upvote</button></td>
                <td><button class="ElementList-downvote" data-value="{{this.mId}}">Downvote</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>

    <h4 id="commented">Your Comments</h4>
    <table class="table">
        <tbody>
            {{#each mCommentData}}
            <tr>
                <!-- This element has info on all the values in mData -->
                <td>{{this.mComment}}</td>
                <td>{{this.mCreateTime}}</td>
                <td>{{this.mUsername}}</td>
            </tr>
            {{/each}}
        </tbody>
    </table>

</div>