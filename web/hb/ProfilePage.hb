<nav class="xyz">
<div class="container-fluid well span6">
	<div class="row-fluid">
        <div class="span2" >
		    <img src="https://image.flaticon.com/icons/png/128/149/149071.png" class="img-circle">
        </div>
        
        {{#each mMessageData}}

        <div class="span8">
            <h3>{{this.mUsername}}</h3>
            <h6>Email: {{this.mEmail}}</h6>
            <h6>Real Name: {{this.mRealName}}</h6>
            <h6>{{this.mProfile}}</h6>
            <li>
                    <a class="btn btn-link" id="ProfilePage-editBio">
                        Edit Bio
                    </a>
            </li>
        </div>

        {{/each}}
    </div>
</div>
</nav>
