<div class="container" id="mainContainer">
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-6">
                    <p class="subject-feed">{{this.mProfileData.mUsername}}</p>
                    <p>{{this.mProfileData.mRealName}}</p>
                    <p><a href="mailto:{{this.mProfileData.mEmail}}">{{this.mProfileData.mEmail}}</a></p>
                    <p><button id="buttonEditBio" type="button" class="btn btn-default btn-xs" data-value="{{this.mProfileData.mUsername}}" style="display: none">Edit Bio</button></p>
                </div>
                <div class="col-xs-6">
                    <img src="https://forum.wholetomato.com/mira/woman.png" class="img-responsive pull-right" />
                </div>
            </div>
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-body">
            <p class="subject-feed">Buzzes</p>
            {{#each mMessageData}}
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-6">
                            <p class="subject-feed">{{this.mSubject}}</p>
                            <p>{{this.mMessage}}
                                {{#if this.mUrl}}
                                    <a href="{{this.mUrl}}" data-toggle="tooltip" title="{{this.mUrl}}" target="_blank"><span class="glyphicon glyphicon-link"></span></a>
                                {{/if}}
                            </p>
                            <p class="date-feed">{{this.mCreateTime}}</p>
                            <p>
                                <a href="#" data-toggle="tooltip" title="Vote count"><span class="badge">{{this.mVotes}}</span></a>
                            </p>
                        </div>
                        <div class="col-xs-6">
                            <img id="img{{this.mId}}" src="" class="img-responsive thumbnail img-feed pull-right" style="display: none" />
                        </div>
                    </div>
                </div>
            </div>
            {{/each}}
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-body">
            <p class="subject-feed">Buzzes Voted Up</p>
            {{#each mLikedData}}
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-6">
                            <p class="subject-feed">{{this.mSubject}}</p>
                            <p>{{this.mMessage}}
                                {{#if this.mUrl}}
                                    <a href="{{this.mUrl}}" data-toggle="tooltip" title="{{this.mUrl}}" target="_blank"><span class="glyphicon glyphicon-link"></span></a>
                                {{/if}}
                            </p>
                            <p class="date-feed">{{this.mCreateTime}}</p>
                            <p>
                                <a href="#" data-toggle="tooltip" title="Vote count"><span class="badge">{{this.mVotes}}</span></a>
                            </p>
                        </div>
                        <div class="col-xs-6">
                            <img id="img{{this.mId}}" src="" class="img-responsive thumbnail img-feed pull-right" style="display: none" />
                        </div>
                    </div>
                </div>
            </div>
            {{/each}}
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-body">
            <p class="subject-feed">Buzzes Voted Down</p>
            {{#each mDisLikedData}}
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-6">
                            <p class="subject-feed">{{this.mSubject}}</p>
                            <p>{{this.mMessage}}
                                {{#if this.mUrl}}
                                    <a href="{{this.mUrl}}" data-toggle="tooltip" title="{{this.mUrl}}" target="_blank"><span class="glyphicon glyphicon-link"></span></a>
                                {{/if}}
                            </p>
                            <p class="date-feed">{{this.mCreateTime}}</p>
                            <p>
                                <a href="#" data-toggle="tooltip" title="Vote count"><span class="badge">{{this.mVotes}}</span></a>
                            </p>
                        </div>
                        <div class="col-xs-6">
                            <img id="img{{this.mId}}" src="" class="img-responsive thumbnail img-feed pull-right" style="display: none" />
                        </div>
                    </div>
                </div>
            </div>
            {{/each}}
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-body">
            <p class="subject-feed">Comments Written</p>
            {{#each mCommentData}}
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-6">
                            <p>{{this.mUsername}}</p>
                            <p>{{this.mComment}}</p>
                            <p class="date-feed">{{this.mCreateTime}}</p>
                        </div>
                    </div>
                </div>
            </div>
            {{/each}}
        </div>
    </div>
</div>