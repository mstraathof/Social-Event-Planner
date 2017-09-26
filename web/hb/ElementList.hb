<div class="panel panel-default" id="ElementList">
    <table class="table">
        <tbody>
            {{#each mData}}
            <tr>
                <!-- This element has info on all the values in mData -->
                <td class ="ElementList-editbtn" id ="ID-{{this.mId}}"data-value="{{this.mId}}"data-message="{{this.mMessage}}"data-subject="{{this.mSubject}}" data-createTime="{{this.mCreateTime}} data-votes="{{this.mVotes}}">{{this.mSubject}}</td>
                <td>{{this.mCreateTime}}</td>
                <td>{{this.mVotes}}</td>
                <!-- Data For both Edit and Delete Buttons on each Entry
                <td><button class="ElementList-editbtn" data-value="{{this.mId}}"data-message="{{this.mMessage}}"data-subject="{{this.mSubject}}">Edit</button></td>
                -->
                <td><button class="ElementList-upvote" data-value="{{this.mId}}">Upvote</button></td>
                <td><button class="ElementList-downvote" data-value="{{this.mId}}">Downvote</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
</div>