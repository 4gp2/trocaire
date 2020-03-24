let dbRes;
const map = L.map('map').setView([0, 0], 2);

const disableCreateAccountButton = disable => {
  document.getElementById('create-admin').disabled = disable;
  document.getElementById('create-worker').disabled = disable;
};

const createNewUser = async admin => {
  const token = await firebase.auth().currentUser.getIdToken(true);
  const res = await axios.post('/api/newuser', { admin, token });
  if (res.error) {
    return;
  }
  if (res.data.error) {
    return;
  }

  document.getElementById('acc-type').textContent = res.data.details.isAdmin
    ? 'Admin'
    : 'Health worker';
  document.getElementById('acc-uid').textContent = res.data.details.uid;
  document.getElementById('acc-password').textContent =
    res.data.details.initialPassword;

  document.getElementById('user-details').hidden = false;
  document.getElementById('password-alert-area').hidden = false;
  disableCreateAccountButton(true);
};

const bootstrapElements = () => {
  document
    .getElementById('year')
    .setAttribute('max', new Date().getFullYear().toString());

  document.getElementById('logout').addEventListener('click', async e => {
    e.preventDefault();
    await logout();
  });

  const patientListContainer = document.getElementById('plist');
  const patientTemplate = document
    .querySelector('template')
    .content.cloneNode(true);
  const patientDetailsContainer = document.getElementById('pdetails');

  document.getElementById('searchB').addEventListener('click', async e => {
    e.preventDefault();
    patientListContainer.innerHTML = '';
    const lastName = document.getElementById('lName').value;
    const firstName = document.getElementById('fName').value;
    const year = document.getElementById('year').value.toString();
    const month = document.getElementById('month').value.toString();
    const day = document.getElementById('day').value.toString();

    if (!lastName || !firstName || !year || !month || !day) {
      return;
    }

    const token = await firebase.auth().currentUser.getIdToken(true);
    dbRes = await axios.post('/api/patient', {
      token,
      lastName,
      firstName,
      year,
      month,
      day,
    });

    if (dbRes.data.error) {
      patientListContainer.appendChild(
        patientTemplate.getElementById('noPatientFound'),
      );
      console.log('no search found');
    } else {
      const nameFound = patientTemplate.getElementById('pNameFound');
      nameFound.textContent = `${dbRes.data.firstName} ${dbRes.data.lastName}`;
      patientListContainer.appendChild(nameFound);
    }
    document.getElementById('plist-container').hidden = false;
  });

  patientTemplate
    .getElementById('pNameFound')
    .addEventListener('click', async e => {
      e.preventDefault();
      patientDetailsContainer.appendChild(document.createElement('form'));
      dbRes.data.forEach(element => {
        const contentRow = patientTemplate.getElementById('pContentRow');
        contentRow.querySelector('label').textContent = element.key;
        contentRow.querySelector('input').setAttribute('value', element.value);
        patientDetailsContainer
          .querySelector('form')
          .appendChild(patientTemplate.getElementById('pContentRow'));
      });
    });

  document.getElementById('password-ack').addEventListener('click', _e => {
    disableCreateAccountButton(false);
    document.getElementById('password-alert-area').hidden = true;
  });
  document
    .getElementById('create-admin')
    .addEventListener('click', async _e => await createNewUser(true));
  document
    .getElementById('create-worker')
    .addEventListener('click', async _e => await createNewUser(false));

  document
    .getElementById('mapview-a')
    .addEventListener('click', _e => setTimeout(() => map.invalidateSize(), 0));
};

//Data api
////Dropdown JS
let dateReq;
let diseaseReq;
let villageReq;

$('.dropdown-menu a').click(function() {
  $(this)
    .parents('.dropdown')
    .find('.btn')
    .html($(this).text());
  const dashPage = $(this)
    .parents('.dropdown')
    .attr('id')
    .match(/[A-Z][a-z]+/g)[0];

  dateReq = $(`#${dashPage}DateDropdown`)
    .find('.btn')
    .text();
  diseaseReq = $(`#${dashPage}DiseaseDropdown`)
    .find('.btn')
    .text();
  if (dashPage === 'Breakdown') {
    villageReq = $(`#${dashPage}VillageDropdown`)
      .find('.btn')
      .text();
    graphRequest(dashPage, dateReq, diseaseReq, villageReq);
  } else {
    graphRequest(dashPage, dateReq, diseaseReq, '');
  }
});

const graphRequest = async (page, date, disease, village) => {
  //console.log("Screen:" + page);
  //console.log(date);
  console.log("Disease: "+ disease);
  console.log("Village: "+ village);

  var start = "";
  var end = new Date();
  if (date === "THIS WEEK"){
    var d = end;
    var day = d.getDay(), diff = d.getDate() - day + (day == 0 ? -6:1);
    start = new Date(d.setDate(diff));
  }
  else if (date === "THIS MONTH") {
    var d = end;
    start = new Date(d.getFullYear(), d.getMonth(), 1);
  }
  else if (date === "THIS YEAR") {
    var d = end;
    start = new Date(d.getFullYear(), 0, 1);
  }
  else if (date === "ALL TIME") {
    start = new Date("Mar 01 2020 00:00:00 GMT")
    end = new Date("Mar 31 2020 00:00:00 GMT");
  }

   console.log("Start date: "+ start)
   console.log("End date: "+ end)

  const token = await firebase.auth().currentUser.getIdToken(true);
  const dataReqRes = await axios.post('/api/graph', {
    token,
    disease,
    village,
    start,
    end,
  });
  // Draw graphs
  if (page === 'Overview') {
    // console.log(dataReqRes)
    // console.log(dataReqRes.data.allPatients)
    var obj = dataReqRes.data.allPatients;
    var people = Object.values(obj);
    var locations = Object.keys(obj)
    var counts = people.map(x => x.length);
    // console.log(map1)
    overviewGraph(locations,counts)

  } else if (page === 'Breakdown') {
  } else if (page === 'Map') {
  }
  console.log('Finished Request');
  console.log('Finished Function');
};

//data request api
// const dataReqRes = await axios.post('/api/data', {
//   disease,
//   village,
//   start,
//   end
// });
 const overviewGraph = (labels, data) =>{
   $('#chart2').remove(); // this is my <canvas> element
   $('#overviewChartContainer').append('<canvas class="my-4" id="chart2" width="300" height="200"></canvas>')

   var chart = new Chart(document.getElementById('chart2'), {
     type: 'bar',
     data: {
       labels: labels,
       datasets: [
         {
           data: data,
           lineTension: 0,
           backgroundColor: ['red', 'blue', 'green', 'orange', 'black'],
           borderColor: 'transparent',
           borderWidth: 4,
           pointBackgroundColor: '#007bff',
         },
       ],
     },
     options: {
       scales: {
         yAxes: [
           {
             ticks: {
               beginAtZero: true,
             },
           },
         ],
       },
       legend: {
         display: false,
       },
     },
   });

  chart.update();
 }

const bootstrapGraphs = () => {
  new Chart(document.getElementById('chart1'), {
    type: 'bar',
    data: {
      labels: ['<18', '18-30', '30-50', '50-65', '65+'],
      datasets: [
        {
          data: [100, 340, 645, 432, 320],
          lineTension: 0,
          backgroundColor: ['red', 'blue', 'green', 'orange', 'black'],
          borderColor: 'transparent',
          borderWidth: 4,
          pointBackgroundColor: '#007bff',
        },
      ],
    },
    options: {
      scales: {
        yAxes: [
          {
            ticks: {
              beginAtZero: true,
            },
          },
        ],
      },
      legend: {
        display: false,
      },
    },
  });

  new Chart(document.getElementById('chart2'), {
    type: 'bar',
    data: {
      labels: ['Village A', 'Village B', 'Village C', 'Village D'],
      datasets: [
        {
          data: [15339, 21345, 18483, 24003],
          lineTension: 0,
          backgroundColor: ['red', 'blue', 'green', 'orange', 'black'],
          borderColor: 'transparent',
          borderWidth: 4,
          pointBackgroundColor: '#007bff',
        },
      ],
    },
    options: {
      scales: {
        yAxes: [
          {
            ticks: {
              beginAtZero: true,
            },
          },
        ],
      },
      legend: {
        display: false,
      },
    },
  });

  new Chart(document.getElementById('chart3'), {
    type: 'pie',
    data: {
      labels: ['Male', 'Female'],
      datasets: [
        {
          data: [38, 62],
          lineTension: 0,
          backgroundColor: ['blue', 'orange'],
          borderColor: 'Transparent',
          borderWidth: 4,
          pointBackgroundColor: '#007bff',
        },
      ],
    },
    options: {
      scales: {
        yAxes: [
          {
            ticks: {
              beginAtZero: true,
            },
          },
        ],
      },
      legend: {
        display: false,
      },
    },
  });
};

const bootstrapMap = () => {
  // add the OpenStreetMap tiles
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution:
      '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap contributors</a>',
  }).addTo(map);
  //
  // // show the scale bar on the lower left corner
  L.control.scale().addTo(map);

  // // show a marker on the map
  L.marker([0, 0])
    .bindPopup('The center of the world')
    .addTo(map);

  map.invalidateSize();
};

bootstrapElements();
bootstrapGraphs();
bootstrapMap();
