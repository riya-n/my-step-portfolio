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
      const cuisineName = cuisine.charAt(0).toUpperCase() + cuisine.substring(1);
      data.addRow([cuisineName, cuisineVotes[cuisine]]);
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

/**
 * Validates that the select value is not empty before making POST request.
 */
function checkSelect() {
  const value = document.getElementById("cuisine-select").value;
  if (value === "") {
    return false;
  }
}

/**
 * Adds the user's vote to the database and updates the chart accordingly. If the
 * user has already voted then their vote is just updated (this way they cannot vote twice).
 */
function addCuisineVote() {
  const selectVal = document.getElementById("cuisine-select").value;
  if (selectVal !== "") {
    let userId = getCookie("userId");
    let cuisineVote = getCookie("cuisineVote");
    console.log(document.cookie);
    if (cuisineVote !== selectVal) {
      if (userId === "") {
        userId = Date.now().toString();
        document.cookie = 'userId=' + userId;
      }
      cuisineVote = selectVal;
      document.cookie = 'cuisineVote=' + selectVal;
      console.log(document.cookie);

      let cuisineVoteJson = {
          'userId': userId,
          'cuisine': cuisineVote
      };

      fetch('/cuisine-data', {method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(cuisineVoteJson)
        })
        .then(response => response.text()).then((data) => {
          drawChart();
        })
    }
  }
}

/** Returns the value mapped to the inputted parameter. */
function getCookie(name) {
  let key = name + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let cookies = decodedCookie.split(';');
  for(let i = 0; i < cookies.length; i++) {
    let cookie = cookies[i];
    cookie = cookie.trim();
    if (cookie.indexOf(key) == 0) {
      return cookie.substring(key.length);
    }
  }
  return "";
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

/**
 * Drops the markers so that the user can see them falling.
 * The pins indicate the location of the courts. You can also
 * click on them to get information about the name and address.
 * The circles indicate the number of courts (if the property is given).
 * Also, the radius of the circle is an indication of the number of courts.
 */
function addMarkerWithTimeout(court, timeout) {
  window.setTimeout(function() {
    courtsMarkers.push(new google.maps.Marker({
      position: { lat: parseFloat(court.lat), lng: parseFloat(court.lon) },
      map: map,
      animation: google.maps.Animation.DROP,
      title: court.Name
    }).addListener('click', function() {
        new google.maps.InfoWindow({
          content: '<div style="font-weight:bold;">' + court.Name +
            '</div><div style="font-style:italic;">' + court.Location + '</div>'
        }).open(map, this);
    }));
    if (court.hasOwnProperty('Num_of_Courts')) {
      let location = new google.maps.LatLng(parseFloat(court.lat), parseFloat(court.lon));
      courtsMarkers.push(new google.maps.Circle({
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#FF0000',
        fillOpacity: 0.35,
        map: map,
        center: location,
        radius: parseInt(court.Num_of_Courts) * 1000
      }));
    }
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
