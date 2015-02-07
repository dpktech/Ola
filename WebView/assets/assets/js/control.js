(function(){
  return olaApi = {
    randomDecimal: function(){
      return (Math.random() * (0.120 - 0.0200) + 0.0200).toFixed(5)/100;
    },
    getRandomLatLang: function(lat, lang, map, a){
      var randomDecimal = olaApi.randomDecimal(),
          newLat = lat + (a ? randomDecimal: -(randomDecimal)),
          newLang = lang + (a ? -(randomDecimal): randomDecimal),
          arr = ['auto', 'sedan', 'prime', 'mini'],
          key = Math.round(Math.random()*4),
          randomCategory = arr[key];
      olaApi.locationOfCab[key] = {
        lat: newLat,
        lang: newLang,
        eta: key + 5 +" mins",
        id: Math.round(Math.random()*4000000000) + 1,
        category: randomCategory
      };

      setTimeout(function(){
        var obj = {lat: map.getCenter().lat(), lang: map.getCenter().lng()};
        if (olaApi.locationOfCabMap) {
          $.each(olaApi.locationOfCabMap, function(mark, marker){
            if (marker) {
              marker.setMap(null);
            }
          });
          delete(olaApi.locationOfCabMap[key]);
        }

        for (var i = 0; i < olaApi.locationOfCab.length; i++) {
          var locations = olaApi.locationOfCab[i];
          if (typeof locations !== 'undefined') {
            olaApi.locationOfCabMap[i] = new google.maps.Marker({
              position: new google.maps.LatLng(locations.lat, locations.lang),
              map: map,
              icon: 'file:///android_asset/assets/img/'+locations.category+'.png',
            });
          }
        }
        olaApi.getRandomLatLang(obj.lat, obj.lang, map, !a);
      }, key + 5000);
    },
    locationOfCab: [],
    locationOfCabMap: []
  }
})();

(function(){
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
          olaApi.getRandomLatLang(position.coords.latitude, position.coords.longitude, map, true);
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
      ola.element.innerHTML('<h1 style="margin-top: 15%;"><center>'+content+'</center></h1>');
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
