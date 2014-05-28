function Controler () {
    var server = "serverRequest";
    
    var name;
    var password;
    
    function main () {

        $("#btnRegister").click( registration );
        $("#btnLogIn").click( logIn );
        $("#btnLogOut").click( logOut );
        $("#addRoute").click( addRoute );

    };
    
    function addRoute () {
        var destination = server + "/logOut";
        var route = {
            name:"routenName"
        };
        serverRequest ( route, destination, showOutput );
    }
    
    function logOut () {
        var destination = server + "/logOut";
        
        serverRequest ( null, destination, showOutput );
    }
    
    function logIn () {
        readInputFealds();
        
        var destination = server + "/logIn";
        
        var user = {
            name : name,
            password : password
        };
        
        serverRequest ( user, destination, showOutput );
    }
    
    function registration () {
        readInputFealds();
        
        var destination = server + "/registration";
        
        var user = {
            name : name,
            password : password
        };
        
        serverRequest ( user, destination, showOutput );
    }
    
    function readInputFealds () {
        name = $("#inputUserName").val();
        password = $("#inputUserPass").val();
    }
    
    function showOutput ( json ) {
        console.log ( json );
        $("#serverOut").html(
                $("#serverOut").html() + "<br>" + json.serverMessge
        );
    }
    
    function serverRequest ( json, destination, callback ) {
    
        $.ajax({
            type: "POST",
            url: destination,
            data: JSON.stringify ( json ),
            dataType: "json",
            error: function ( info, b, c ) {
                console.log(info);
                console.log(b);
                console.log(c);
            }
        }).done ( callback ).fail ( function ( info ) {
            console.log(info);
        });
    }
    
    main ();
};