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
let courtsInfo;

/** Creates a map and adds it to the page. */
function createMap() {
  fetch('/basketball-courts').then(response => response.json()).then((courts) => {
    map = new google.maps.Map(document.getElementById('map-container'), {
      center: { lat: 40.7128, lng: -74.0060 },
      zoom: 10,
      styles: style
    });

    courtsInfo = courts;
  });
}

let courtsMarkers = [];

/** add markers for courts */
function showCourtsMarkers() {
  clearCourtsMarkers();
  for (let i = 0; i < courtsInfo.length; i++) {
    addMarkerWithTimeout(courtsInfo[i], i * 200);
  }
}

/** drop the markers so that the user can see them falling */
function addMarkerWithTimeout(court, timeout) {
  window.setTimeout(function() {
    courtsMarkers.push(new google.maps.Marker({
      position: { lat: parseFloat(court.lat), lng: parseFloat(court.lon) },
      map: map,
      animation: google.maps.Animation.DROP,
      title: court.Name
    }));
  }, timeout);
}

/** clear the markers (so they can be dropped again) */
function clearCourtsMarkers() {
  for (let i = 0; i < courtsMarkers.length; i++) {
    courtsMarkers[i].setMap(null);
  }
  courtsMarkers = [];
}

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
