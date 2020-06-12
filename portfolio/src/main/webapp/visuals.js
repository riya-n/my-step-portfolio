google.charts.load('current', { 'packages': ['corechart'] });
google.charts.setOnLoadCallback(drawChart);

/** Creates the pie chart and adds it to the page. */
function drawChart() {
  fetch('cuisine-data').then(response => response.json())
    .then((cuisineVotes) => {

    let data = new google.visualization.DataTable();
    data.addColumn('string', 'Cuisine');
    data.addColumn('number', 'Votes');
    Object.keys(cuisineVotes).forEach((cuisine) => {
      data.addRow([cuisine, cuisineVotes[cuisine]]);
    });

    const options = {
      pieHole: 0.4,
      pieSliceTextStyle: {
        color: '#f5f6fa'
      },
      backgroundColor: '#a6ded2',
      legend: 'none'
    };

    const chart = new google.visualization.PieChart(document.getElementById('chart-container'));
    chart.draw(data, options);
  })
}

let map;

/** Creates a map and adds it to the page. */
function createMap() {
  map = new google.maps.Map(document.getElementById('map-container'), {
    center: { lat: 1.3521, lng: 103.8198 },
    zoom: 11,
    styles: style
  });
}

let restuarantMarkers = [];

/** add markers for restaurants */
function showRestaurantMarkers() {
  clearRestaurantMarkers();
  for (var i = 0; i < restaurantInfo.length; i++) {
    addMarkerWithTimeout(restaurantInfo[i].name,
    restaurantInfo[i].location, i * 200);
  }
}

/** drop the markers so that the user can see them falling */
function addMarkerWithTimeout(name, location, timeout) {
  window.setTimeout(function() {
    restuarantMarkers.push(new google.maps.Marker({
      position: location,
      map: map,
      animation: google.maps.Animation.DROP,
      title: name
    }));
  }, timeout);
}

/** clear the markers (so they can be dropped again) */
function clearRestaurantMarkers() {
  for (var i = 0; i < restuarantMarkers.length; i++) {
    restuarantMarkers[i].setMap(null);
  }
  restuarantMarkers = [];
}

/** name and location of the restuarant for the map markers */
const restaurantInfo = [
  {
    name: 'Nakhon Kitchen',
    location: { lat: 1.3104, lng: 103.7948 }
  }
]

/** map styling */
const style = [
    { elementType: 'geometry', stylers: [{ color: '#242f3e' }] },
    { elementType: 'labels.text.stroke', stylers: [{ color: '#242f3e' }] },
    { elementType: 'labels.text.fill', stylers: [{ color: '#746855' }] },
    {
        featureType: 'administrative.locality',
        elementType: 'labels.text.fill',
        stylers: [{ color: '#d59563' }]
    },
    {
        featureType: 'poi',
        elementType: 'labels.text.fill',
        stylers: [{ color: '#d59563' }]
    },
    {
        featureType: 'poi.park',
        elementType: 'geometry',
        stylers: [{ color: '#263c3f' }]
    },
    {
        featureType: 'poi.park',
        elementType: 'labels.text.fill',
        stylers: [{ color: '#6b9a76' }]
    },
    {
        featureType: 'road',
        elementType: 'geometry',
        stylers: [{ color: '#38414e' }]
    },
    {
        featureType: 'road',
        elementType: 'geometry.stroke',
        stylers: [{ color: '#212a37' }]
    },
    {
        featureType: 'road',
        elementType: 'labels.text.fill',
        stylers: [{ color: '#9ca5b3' }]
    },
    {
        featureType: 'road.highway',
        elementType: 'geometry',
        stylers: [{ color: '#746855' }]
    },
    {
        featureType: 'road.highway',
        elementType: 'geometry.stroke',
        stylers: [{ color: '#1f2835' }]
    },
    {
        featureType: 'road.highway',
        elementType: 'labels.text.fill',
        stylers: [{ color: '#f3d19c' }]
    },
    {
        featureType: 'transit',
        elementType: 'geometry',
        stylers: [{ color: '#2f3948' }]
    },
    {
        featureType: 'transit.station',
        elementType: 'labels.text.fill',
        stylers: [{ color: '#d59563' }]
    },
    {
        featureType: 'water',
        elementType: 'geometry',
        stylers: [{ color: '#17263c' }]
    },
    {
        featureType: 'water',
        elementType: 'labels.text.fill',
        stylers: [{ color: '#515c6d' }]
    },
    {
        featureType: 'water',
        elementType: 'labels.text.stroke',
        stylers: [{ color: '#17263c' }]
    }
]
