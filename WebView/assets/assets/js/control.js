(function(){
 console.log("alert....");
  return ola = {
    initializeMap: function() {
      ola.element.style.height = window.innerHeight - 51;
      ola.element.style.width = window.innerWidth;

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
      var map = new google.maps.Map(ola.element, mapOptions);
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
          var marker = new google.maps.Marker({
            position: pos,
            map: map,
            draggable: false,
            icon: 'file:///android_asset/assets/img/map_marker.png',
            animation: google.maps.Animation.DROP
          });
          marker.bindTo('position', map, 'center');
          map.setCenter(pos);
        }, function() {
          ola.handleNoGeolocation(true);
        });
      } else {
        // Browser doesn't support Geolocation
        ola.handleNoGeolocation(false);
      }
    },
    handleNoGeolocation: function (errorFlag) {
      var content =  (errorFlag)? 'The Geolocation service failed. Please Enable location service.' : 'Error: Your browser doesn\'t support geolocation.';
      Android.showToast(content);
    },
    element: document.getElementById('map-canvas')
  };
})();
google.maps.event.addDomListener(window, 'load', ola.initializeMap);

$(document).ready(function(){
   $(".navbar-btn.settings").bind('click', function(event){
     event.preventDefault();
     $("#settings-modal").modal("show");
   });
});
