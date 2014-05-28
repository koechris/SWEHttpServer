function Controler () {
    var server = "serverRequest";
    
    var name;
    var password;
    var routeName;
    
    function main () {

        $("#btnRegister").click( registration );
        $("#btnLogIn").click( logIn );
        $("#btnLogOut").click( logOut );
        $("#addRoute").click( addRoute );
        $("#getRoutes").click( getRoutes );
        $("#addTrackPoint").click( addTrackPoint );
        $("#getTrackPoints").click( getTrackPoints );

    };
    
    // füge trackpoint zur rute mit dem index 0 hinzu
    function addTrackPoint () {
        var destination = server + "/addTrackPoint";
        var send = {
            routeIndex:0,
            trackPoint:new TrackPoint()
        };
        console.log(send);
        serverRequest ( send, destination );
    }
    
    // hole trackpoints von rute mit dem index 0
    function getTrackPoints () {
        var destination = server + "/getTrackPoints";
        var route = {
            index:0
        };
        serverRequest ( route, destination );
    }
    
    function getRoutes () {
        var destination = server + "/getRoutes";
        var route = {
            name:routeName
        };
        serverRequest ( route, destination );
    }
    
    function addRoute () {
        readInputFealds();
        var destination = server + "/addRoute";
        var route = {
            name:routeName
        };
        serverRequest ( route, destination );
    }
    
    function logOut () {
        var destination = server + "/logOut";
        
        serverRequest ( null, destination );
    }
    
    function logIn () {
        readInputFealds();
        
        var destination = server + "/logIn";
        
        var user = {
            name : name,
            password : password
        };
        
        serverRequest ( user, destination );
    }
    
    function registration () {
        readInputFealds();
        
        var destination = server + "/registration";
        
        var user = {
            name : name,
            password : password
        };
        
        serverRequest ( user, destination );
    }
    
    function readInputFealds () {
        name = $("#inputUserName").val();
        password = $("#inputUserPass").val();
        routeName = $("#inputRoute").val();
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
        }).done ( function ( json ) {
            console.log(json);
        }).fail ( function ( info ) {
            console.log(info);
        });
    }
    
    main ();
};

function TrackPoint () {
    this.latitude = parseInt(Math.random()*100000000000);
    this.longitude = parseInt(Math.random()*100000000000);
    this.altitude = parseInt(Math.random()*100000000000);
    this.timestamp = parseInt(Math.random()*100000000000);
    this.isCheckPoint = false;
}