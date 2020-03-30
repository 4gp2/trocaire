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

  // const patientListContainer = document.getElementById('plist');
  const patientTemplate = document
    .querySelector('template')
    .content.cloneNode(true);
  const patientDetailsContainer = document.getElementById('pdetails');

  document.getElementById('searchB').addEventListener('click', async e => {
    e.preventDefault();
    // patientListContainer.innerHTML = '';
    const lastName = document.getElementById('lName').value;
    const firstName = document.getElementById('fName').value;
    const year = document.getElementById('year').value.toString();
    const month = document.getElementById('month').value.toString();
    const day = document.getElementById('day').value.toString();

    if (!lastName || !firstName || !year || !month || !day) {
      document.getElementById('noPatients').hidden = false;
      return;
    }
    console.log(lastName);
    console.log(firstName);
    console.log(year);
    const token = await firebase.auth().currentUser.getIdToken(true);
    dbRes = await axios.post('/api/patient', {
      token,
      lastName,
      firstName,
      year,
      month,
      day,
    });

    console.log(dbRes.data);

    if (dbRes.error || dbRes.data.error) {
      document.getElementById('noPatients').hidden = false;
    } else {
      console.log(dbRes.data);
      document.getElementById('noPatients').hidden = true;
      patientDetailsContainer.innerHTML = '';
      // const nameFound = patientTemplate.getElementById('pNameFound');
      // nameFound.textContent = `${dbRes.data.firstName} ${dbRes.data.lastName}`;
      // patientListContainer.appendChild(nameFound);
    }
  });

  // patientTemplate
  //   .getElementById('pNameFound')
  //   .addEventListener('click', async e => {
  //     e.preventDefault();
  //     patientDetailsContainer.appendChild(document.createElement('form'));
  //     dbRes.data.forEach(element => {
  //       const contentRow = patientTemplate.getElementById('pContentRow');
  //       contentRow.querySelector('label').textContent = element.key;
  //       contentRow.querySelector('input').setAttribute('value', element.value);
  //       patientDetailsContainer
  //         .querySelector('form')
  //         .appendChild(patientTemplate.getElementById('pContentRow'));
  //     });
  //   });

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

const adminWarning = () =>
  firebase.auth().onAuthStateChanged(async user => {
    const token = await user.getIdToken(true);
    const diseases = ['Cholera', 'Polio', 'Measles', 'Malaria', 'COVID-19'];
    let highest_cases = 0;
    let result, sum = 0;
    for (const d of diseases) {
      result = await axios.post('/api/graph', {
        token,
        disease: d,
        start: new Date('2020-03-02'),
        end: new Date('2020-03-31'),
      });
      if (result.error || result.data.error) {
        break;
      }

      for (var v in result.data.allPatients) {
        sum += result.data.allPatients[v].length;
      }

      if (sum > highest_cases) {
        highest_cases = sum;
        highest_disease = d;
      }
    }

    const warning_box = document.getElementById('warning_box');
    warning_box.innerHTML = '';
    if (highest_cases < 80) {
      warning_box.innerText = 'No Major Increase of Disease Cases Detected';
      warning_box.style.backgroundColor = 'lightgreen';
    }
    if (highest_cases >= 80 && highest_cases < 160) {
      warning_box.innerText =
        'There is an Increase of ' +
        highest_disease.toUpperCase() +
        ' Cases Detected. Please Check Dashboard';
      warning_box.style.backgroundColor = 'orange';
    }
    if (highest_cases >= 160) {
      warning_box.innerText =
        'There is High Outbreak of ' +
        highest_disease.toUpperCase() +
        ' Detected. Please Check Dashboard';
      warning_box.style.backgroundColor = 'red';
    }
  });

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

  console.log(page);
  var start = '';
  var end = new Date();
  if (date === 'THIS WEEK') {
    var d = end;
    var day = d.getDay(),
      diff = d.getDate() - day + (day == 0 ? -6 : 1);
    start = new Date(d.setDate(diff));
  } else if (date === 'THIS MONTH') {
    var d = end;
    start = new Date(d.getFullYear(), d.getMonth(), 1);
  } else if (date === 'THIS YEAR') {
    var d = end;
    start = new Date(d.getFullYear(), 0, 1);
  } else if (date === 'ALL TIME') {
    start = new Date('Mar 01 2020 00:00:00 GMT');
    end = new Date('Mar 31 2020 00:00:00 GMT');
  }

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
    var obj = dataReqRes.data.allPatients;
    var people = Object.values(obj);
    var locations = Object.keys(obj);
    var counts = people.map(x => x.length);
    overviewGraph(locations, counts);
  } else if (page === 'Breakdown') {
    var obj = dataReqRes;
    var patientData = obj.data.villagePatients;

    const countFemale = patientData.filter(o => o.sex === 'Female').length;
    const countMale = patientData.filter(o => o.sex === 'Male').length;

    let ages = patientData.map(a => Math.floor(a.dob._seconds / 31536000));
    const count18 = ages.filter(o => o <= 18).length;
    const count30 = ages.filter(o => o <= 30).length - count18;
    const count50 = ages.filter(o => o <= 50).length - count18 - count30;
    const count65 =
    ages.filter(o => o <= 65).length - count18 - count30 - count50;
    const countAbove = ages.filter(o => o > 65).length;

    breakdownGraph(
      [countMale, countFemale],
      [count18, count30, count50, count65, countAbove],
    );
  } else if (page === 'Map') {
      var coords = [];
      var dates = [];
      var obj = dataReqRes.data.allPatients;
      var locations = Object.keys(obj);
      for (var i = 0; i < locations.length; i++) {
        var village = obj[locations[i]];
        for (var j = 0; j < village.length; j++) {
          var person = village[j]
          var diag = person.diagnoses[0]
          var coord = [diag.latitude,diag.longitude]
          coords.push(coord)
          dates.push(diag.date);
          bootstrapMap(coords, dates)
        }
      }

  }
  else if (page === 'List') {
    console.log("sadsdasd")
    $("#mytablecontent tr").remove();
    // var employee = new Array();
    //         employee.push([4, "Billie Jean", "Country4"]);
    //         employee.push([5, "Harish Kumar", "Country5"]);
    //         employee.push([6, "Pankaj Mohan", "Country6"]);
    //         employee.push([7, "Nitin Srivastav", "Country7"]);
    //         employee.push([8, "Ramchandra Verma", "Country8"]);
    //
    //         var tablecontents = "";
    //         for (var i = 0; i < employee.length; i++) {
    //             tablecontents += "<tr>";
    //             for (var j = 0; j < employee[i].length; j++) {
    //                 tablecontents += "<td>" + employee[i][j] + "</td>";
    //             }
    //             tablecontents += "</tr>";
    //         }
    //         document.getElementById("mytablecontent").innerHTML += tablecontents;
  }
};

//data request api
// const dataReqRes = await axios.post('/api/data', {
//   disease,
//   village,
//   start,
//   end
// });
const overviewGraph = (labels, data) => {
  $('#chart2').remove(); // this is my <canvas> element
  $('#overviewChartContainer').append(
    '<canvas class="my-4" id="chart2" width="300" height="200"></canvas>',
  );

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
};

const breakdownGraph = (pieData, ageData) => {
  $('#chart3').remove(); // this is my <canvas> element
  $('#breakdownChartContainer').append(
    '<canvas class="my-4" id="chart3" width="300" height="200"></canvas>',
  );

  var chart = new Chart(document.getElementById('chart3'), {
    type: 'pie',
    data: {
      labels: ['Male', 'Female'],
      datasets: [
        {
          data: pieData,
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
  chart.update();

  $('#chart1').remove(); // this is my <canvas> element
  $('#breakdownChartContainer1').append(
    '<canvas class="my-4" id="chart1" width="300" height="200"></canvas>',
  );

  var chart1 = new Chart(document.getElementById('chart1'), {
    type: 'bar',
    data: {
      labels: ['<18', '18-30', '30-50', '50-65', '65+'],
      datasets: [
        {
          data: ageData,
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
  chart1.update();
};

const bootstrapGraphs = () => {
  graphRequest('Overview', 'THIS MONTH', 'Cholera', '');
  graphRequest('Breakdown', 'THIS MONTH', 'Cholera', 'Dublin');
  graphRequest('Map', 'THIS MONTH', 'Cholera', '');
};

const bootstrapMap = (coords, label) => {
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
  for (var i = 0; i < coords.length; i++) {
    L.marker(coords[i])
      .bindPopup(label[i])
      .addTo(map);
  }


  map.invalidateSize();
};

bootstrapElements();
adminWarning();
