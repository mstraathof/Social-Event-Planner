<nav class="navbar navbar-default">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" 
                data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <!-- Clicking the brand refreshes the page -->
            <a class="navbar-brand" href="/">The Buzz</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li>
                    <!--<a class="btn btn-link" id="Navbar-createAccount">
                        Create Account
                    </a>
                </li>
                <li>
                    <a class="btn btn-link" id="Navbar-logIn">
                        Log In
                    </a>-->
                </li>
            </ul>
        </div>
    </div>
</nav>

  <head>
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id" content=1080316803619-flf753te3n99rv3mh90movqrs3eujk3v.apps.googleusercontent.com>
    <script src="https://apis.google.com/js/platform.js" async defer></script>
  </head>
  <body>
    <div id="Gsignin" class="g-signin2" data-onsuccess="onSignIn" data-theme="dark"></div>
    <script>
      function onSignIn(googleUser) {
        // Useful data for your client-side scripts:
        var profile = googleUser.getBasicProfile();
        console.log("ID: " + profile.getId()); // Don't send this directly to your server!
        console.log('Full Name: ' + profile.getName());
        console.log('Given Name: ' + profile.getGivenName());
        console.log('Family Name: ' + profile.getFamilyName());
        console.log("Image URL: " + profile.getImageUrl());
        console.log("Email: " + profile.getEmail());

        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;
        console.log("ID Token: " + id_token);
         $.ajax({
                type: "POST",
                url: "/tokensignin",
                dataType: "json",
                data: JSON.stringify({ token_id: id_token }),
                success: Navbar.onLoginResponse
            });       
      };
    </script>
 
  </body>

