(function(){
  return olaApi = {
    categoryArray: ['auto', 'sedan', 'prime', 'mini'],
    randomDecimal: function(){
      return (Math.random() * (0.120 - 0.0200) + 0.0200).toFixed(5)/100;
    },
    getRandomLatLang: function(lat, lang, map, a){
      var randomDecimal = olaApi.randomDecimal(),
          newLat = lat + (a ? randomDecimal: -(randomDecimal)),
          newLang = lang + (a ? -(randomDecimal): randomDecimal),
          arr = olaApi.categoryArray,
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
            var infowindow = new google.maps.InfoWindow({
                content: locations.eta,
                maxWidth: 40,
                close: false
            });
            google.maps.event.addListener( olaApi.locationOfCabMap[i], 'click', function() {
              infowindow.open(map, olaApi.locationOfCabMap[i]);
            });
            infowindow.open(map, olaApi.locationOfCabMap[i]);
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

      var placeMyLocation = function (controlDiv, map) {
        // Set CSS for the control border
        var controlUI = document.createElement('div');
        controlUI.setAttribute("class", "btn");
        controlUI.style.background = '#f8f8f8';
        controlUI.style.borderRadius = '25px';
        controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
        controlUI.style.cursor = 'pointer';
        controlUI.style.marginBottom = '25px';
        controlUI.style.marginLeft = '-60px';
        controlUI.style.width = '40px';
        controlUI.style.height = '40px';
        controlUI.title = 'Click to recenter the map';
  //    controlUI.style.backgroundImage = "url('file:///android_asset/assets/img/location.png')";
        controlDiv.appendChild(controlUI);
        // Set CSS for the control interior
        var controlLocationImage =  document.createElement('img');
        controlLocationImage.src = "file:///android_asset/assets/img/location.png";
        controlLocationImage.style.width = '32px';
        controlLocationImage.style.height = '32px';
        controlLocationImage.style.marginTop = "-3px";
        controlLocationImage.style.marginLeft = "-9px";
        controlUI.appendChild(controlLocationImage);
        google.maps.event.addDomListener(controlUI, 'click', function() {
            navigator.geolocation.getCurrentPosition(function(position) {
              var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
              map.setCenter(pos);
              map.setZoom(17);
            });
        });
      };

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
          var mapOptions = {
            zoom: 17,
            center: pos,
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

          var marker = new google.maps.Marker({
            position: pos,
            map: map,
            draggable: false,
            icon: 'file:///android_asset/assets/img/map_marker.png',
            animation: google.maps.Animation.DROP
          });
          marker.bindTo('position', map, 'center');
          map.setCenter(pos);
          var centerControlDiv = document.createElement('div');
          var centerControl = new placeMyLocation(centerControlDiv, map);
          centerControlDiv.index = 1;
          console.log(centerControlDiv);
          map.controls[google.maps.ControlPosition.BOTTOM_LEFT].push(centerControlDiv);
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

   $(".modal .signin").bind('click', function(event){
     event.preventDefault();
     var resp = Android.login();
     Android.showToast("Welcome "+JSON.parse(resp).name);
     $("#settings-modal").modal("hide");
     $(".modal .linkup").html( '<div class="checkbox"><label class="text-success"><h3>'+JSON.parse(resp).name+'</h3></label>');
   });

   $(".modal .signup").bind('click', function(event){
     event.preventDefault();
     $("#settings-modal").modal("hide");
   });

   $("ul#menu1 > li > a").unbind().bind("click", function(event){
     event.preventDefault();
     if ($(this).attr("value") !== 'all') {
        olaApi.categoryArray = [$(this).attr("value"), $(this).attr("value"), $(this).attr("value"), $(this).attr("value")];
     } else {
        olaApi.categoryArray = ['auto', 'sedan', 'prime', 'mini'];
     }
     $('.category.btn').html($(this).text() + '<span class="caret"></span>');
   });
});
