(function(){
 console.log("alert....");
  return ola = {
    initializeMap: function() {
      
      var element = document.getElementById('map-canvas');
      element.style.height = window.innerHeight - 51;
      element.style.width = window.innerWidth;

      var mapOptions = {
        zoom: 17,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        mapTypeControl: false,
        overviewMapControl: false,
        zoomControl: false,
        draggable: true,
        streetViewControl: false,
        scaleControl: false,
        panControl: false
      };
      var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
      if (navigator.geolocation) {
      
        navigator.geolocation.getCurrentPosition(function(position) {
          var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
         console.log("calling...");
          var marker = new google.maps.Marker({
            position: pos,
            map: map,
            draggable: false,
            animation: google.maps.Animation.DROP
          });
          marker.bindTo('position', map, 'center');
          map.setCenter(pos);
        }, function() {
          this.handleNoGeolocation(true);
        });
      } else {
        // Browser doesn't support Geolocation
        this.handleNoGeolocation(false);
      }
    },
    handleNoGeolocation: function (errorFlag) {
      if (errorFlag) {
        var content = 'Error: The Geolocation service failed.';
      } else {
        var content = 'Error: Your browser doesn\'t support geolocation.';
      }
      console.log(content);
    }
  };
})();
google.maps.event.addDomListener(window, 'load', ola.initializeMap);

$(document).ready(function(){

});
