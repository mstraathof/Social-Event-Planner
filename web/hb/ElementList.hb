<div class="panel panel-default" id="ElementList">
    <h2 id="yours">Your Messages/Buzz's</h2>
    <table class="table">
        <tbody>
            {{#each mMessageData}}
            <tr>
                <!-- This element has info on all the values in mData -->
                <td class ="ElementList-editbtn"> {{this.mSubject}}</td>
                <td class="col-md-6">{{this.mMessage}}</td>
                
                <td>{{this.mCreateTime}}</td>
                <td>{{this.mVotes}}</td>


                <!-- Data For both Edit and Delete Buttons on each Entry
                <td><button class="ElementList-editbtn" data-value="{{this.mId}}"data-message="{{this.mMessage}}"data-subject="{{this.mSubject}}">Edit</button></td>
                -->
                <td><button class="ElementList-comments" data-value="{{this.mId}}">Comments</button></td>
                <td><button class="ElementList-profile" data-value="{{this.mUsername}}">{{this.mUsername}}</button></td>
                <td><button class="ElementList-upvote" data-value="{{this.mId}}">Upvote</button></td>
                <td><button class="ElementList-downvote" data-value="{{this.mId}}">Downvote</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>

    <h2 id="liked">Liked Messages</h2>
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

    <h2 id="disliked">Disliked Messages</h2>
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

    <h2 id="commented">Comments</h2>
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