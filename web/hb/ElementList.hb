<div class="panel panel-default" id="ElementList">
    <table class="table">
        <tbody>
            {{#each mData}}
            <tr class="ElementList-editbtn" data-value="{{this.mId}}"data-message="{{this.mMessage}}"data-subject="{{this.mSubject}}">
                <td>{{this.mSubject}}</td>
                <td>Date Submitted: </td>
                <td>Votes: </td>
                <!-- Data For both Edit and Delete Buttons on each Entry
                <td><button class="ElementList-editbtn" data-value="{{this.mId}}"data-message="{{this.mMessage}}"data-subject="{{this.mSubject}}">Edit</button></td>
                <td><button class="ElementList-delbtn" data-value="{{this.mId}}">Delete</button></td>
                -->
            </tr>
            {{/each}}
        </tbody>
    </table>
</div>