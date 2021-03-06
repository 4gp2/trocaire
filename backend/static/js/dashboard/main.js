let dbRes;
const map = L.map('map').setView([0, 0], 2);
const socketIO = io();

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

  const patientTemplate = document.querySelector('template');
  const patientDetailsContainer = document.getElementById('pdetails');
  const noPatientsWarning = document.getElementById('noPatients');

  document.getElementById('searchB').addEventListener('click', async e => {
    e.preventDefault();
    const lastName = document.getElementById('lName').value;
    const firstName = document.getElementById('fName').value;
    const year = document.getElementById('year').value.toString();
    const month = document.getElementById('month').value.toString();
    const day = document.getElementById('day').value.toString();

    if (!lastName || !firstName || !year || !month || !day) {
      noPatientsWarning.hidden = false;
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

    if (dbRes.error || dbRes.data.error) {
      noPatientsWarning.hidden = false;
      return;
    }

    noPatientsWarning.hidden = true;
    patientDetailsContainer.querySelector('img').hidden = true;
    console.log(dbRes.data.patient);
    var content;
    for (var k in dbRes.data.patient) {
      content = dbRes.data.patient[k];
      if (k === 'dob') {
        var dob = new Date(content._seconds);
        content =
          dob.getDate() + '/' + (dob.getMonth() + 1) + '/' + dob.getFullYear();
      }
      if (k === 'diagnoses') {
        var s = '';
        for (var i in dbRes.data.patient.diagnoses[0].symptoms) {
          if (dbRes.data.patient.diagnoses[0].symptoms[i]) {
            s += i + ', ';
          }
        }
        content = s;
      }
      patientDetailsContainer
        .querySelector('#' + k)
        .setAttribute('value', content);
    }
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

const adminWarning = () =>
  firebase.auth().onAuthStateChanged(async user => {
    const token = await user.getIdToken(true);
    const diseases = ['Cholera', 'Polio', 'Measles', 'Malaria', 'COVID-19'];
    let highest_cases = 0;
    let result,
      sum = 0;
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
const buildVillageDropdown = async e => {
  //build village dropdown
  const token = await firebase.auth().currentUser.getIdToken(true);
  const villageReqRes = await axios.post('/api/villages', {
    token,
  });

  var dropContent = '';
  //$('#dialog_title_span').text("new dialog title");
  var locations = villageReqRes.data.villages;
  for (var i = 0; i < locations.length; i++) {
    if (i == 0) {
      $(`#BreakdownVillageDropdown`)
        .find('.btn')
        .text(locations[i]);
      dropContent +=
        '<a class="dropdown-item" href="#">' + locations[i] + '</a>';
    } else {
      dropContent +=
        '<a class="dropdown-item" href="#">' + locations[i] + '</a>';
    }
  }
  document.getElementById('villageDropdownOptions').innerHTML = dropContent;

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

  graphRequest(
    'Breakdown',
    $(`#BreakdownDateDropdown`)
      .find('.btn')
      .text(),
    $(`#BreakdownDiseaseDropdown`)
      .find('.btn')
      .text(),
    $(`#BreakdownVillageDropdown`)
      .find('.btn')
      .text(),
  );
};

const graphRequest = async (page, date, disease, village) => {
  var start = '';
  var end = new Date();
  if (date === 'THIS WEEK') {
    var d = end;
    var day = d.getDay(),
      diff = d.getDate() - day + (day == 0 ? -6 : 1);
    start = new Date(d.setDate(diff));
    end = new Date();
  } else if (date === 'THIS MONTH') {
    var d = end;
    start = new Date(d.getFullYear(), d.getMonth(), 1);
    end = new Date();
  } else if (date === 'THIS YEAR') {
    var d = end;
    start = new Date(d.getFullYear(), 0, 1);
    end = new Date();
  } else if (date === 'ALL TIME') {
    start = new Date('Mar 01 2017 00:00:00 GMT');
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

    //Disease cooccurence
    var diseases = ['Measles', 'Cholera', 'Polio', 'Malaria', 'COVID-19'];
    diseases.splice(diseases.indexOf(disease), 1);
    var diseaseCounts = [];
    if (patientData.length > 0) {
      var diseaseKeys = Object.values(
        patientData[0].diagnoses[0].possibleDiseases,
      );
      var patiensDiseases = [];
      for (var i = 0; i < patientData.length; i++) {
        var countableDisease = Object.values(
          patientData[i].diagnoses[0].possibleDiseases,
        );
        for (var j = 0; j < countableDisease.length; j++) {
          patiensDiseases.push(countableDisease[j]);
        }
      }
      for (var k = 0; k < diseases.length; k++) {
        var count = patiensDiseases.filter(function(x) {
          return x === diseases[k];
        }).length;
        diseaseCounts.push(count);
      }
    }

    //symptom graph
    var symKeys = [];
    var symCounts = [];

    if (patientData.length > 0) {
      symKeys = Object.keys(patientData[0].diagnoses[0].symptoms);
      symKeys.splice(symKeys.indexOf('rash'), 1);
      symKeys.splice(symKeys.indexOf('pain'), 1);
      symKeys.splice(symKeys.indexOf('temperature'), 1);
      var patiensSymptoms = [];
      for (var i = 0; i < patientData.length; i++) {
        patiensSymptoms.push(patientData[i].diagnoses[0].symptoms);
      }
      for (var i = 0; i < symKeys.length; i++) {
        var count = patiensSymptoms.filter(obj => obj[symKeys[i]] === true)
          .length;
        symCounts.push(count);
      }
    }

    //gender graph
    const countFemale = patientData.filter(o => o.sex === 'Female').length;
    const countMale = patientData.filter(o => o.sex === 'Male').length;

    //age graph
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
      symKeys,
      symCounts,
      diseases,
      diseaseCounts,
    );
  } else if (page === 'Map') {
    var coords = [];
    var dates = [];
    var obj = dataReqRes.data.allPatients;
    var locations = Object.keys(obj);
    for (var i = 0; i < locations.length; i++) {
      var village = obj[locations[i]];
      for (var j = 0; j < village.length; j++) {
        var person = village[j];
        var diag = person.diagnoses[0];
        var coord = [diag.latitude, diag.longitude];
        coords.push(coord);
        dates.push(diag.date);
        bootstrapMap(coords, dates);
      }
    }
  } else if (page === 'List') {
    $('#mytablecontent tr').remove();
    var obj = dataReqRes.data.allPatients;
    var locations = Object.keys(obj);
    var tablecontents = '';
    for (var i = 0; i < locations.length; i++) {
      var village = obj[locations[i]];
      tablecontents += '<tr>';
      for (var j = 0; j < village.length; j++) {
        var person = village[j];
        var sym = person.diagnoses[0].symptoms;
        var keys = Object.keys(sym);
        var filtered = keys.filter(function(key) {
          return sym[key];
        });
        var symptomStr = String(filtered).replace(new RegExp(',', 'g'), ', ');
        symptomStr = symptomStr.replace(
          'temperature',
          'temperature: ' + sym.temperature,
        );
        symptomStr = symptomStr.replace(
          'pain',
          'painLevel: ' + sym.pain.painDiscomfortLevel,
        );
        if (!sym.rash.hasRash) {
          symptomStr = symptomStr.replace('rash,', '');
        }
        tablecontents += '<td>' + person.firstName + '</td>';
        tablecontents += '<td>' + person.lastName + '</td>';
        tablecontents +=
          '<td>' + Math.floor(person.dob._seconds / 31536000) + '</td>';
        tablecontents += '<td>' + person.sex + '</td>';
        tablecontents += '<td>' + locations[i] + '</td>';
        tablecontents += '<td>' + symptomStr + '</td>';
        tablecontents += '</tr>';
      }
    }
    document.getElementById('mytablecontent').innerHTML += tablecontents;
  }
};

const overviewGraph = (labels, data) => {
  $('#chart2').remove();
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
          backgroundColor: [
            'red',
            'blue',
            'green',
            'orange',
            'black',
            'coral',
            'cyan',
            'DarkGray',
            'DarkOrange',
            'violet',
            'yellow',
            'lime',
            'teal',
          ],
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
      title: {
        display: true,
        text: 'Disease Count per Location',
      },
    },
  });
};

const breakdownGraph = (
  pieData,
  ageData,
  symLabels,
  symData,
  diseaseLabel,
  diseaseData,
) => {
  $('#chart3').remove();
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
      title: {
        display: true,
        text: 'Sex Distribution',
      },
    },
  });
  chart.update();

  $('#chart1').remove();
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
      title: {
        display: true,
        text: 'Age Range Counts',
      },
    },
  });
  chart1.update();

  $('#chart5').remove();
  $('#breakdownChartContainer5').append(
    '<canvas class="my-4" id="chart5" width="300" height="200"></canvas>',
  );

  var chart5 = new Chart(document.getElementById('chart5'), {
    type: 'bar',
    data: {
      labels: symLabels,
      datasets: [
        {
          data: symData,
          lineTension: 0,
          backgroundColor: [
            'red',
            'blue',
            'green',
            'orange',
            'black',
            'coral',
            'cyan',
            'DarkGray',
            'DarkOrange',
            'violet',
            'yellow',
            'lime',
            'teal',
          ],
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
      title: {
        display: true,
        text: 'Symptom Counts',
      },
    },
  });
  chart5.update();

  $('#chart6').remove();
  $('#breakdownChartContainer6').append(
    '<canvas class="my-4" id="chart6" width="300" height="200"></canvas>',
  );

  var chart6 = new Chart(document.getElementById('chart6'), {
    type: 'bar',
    data: {
      labels: diseaseLabel,
      datasets: [
        {
          data: diseaseData,
          lineTension: 0,
          backgroundColor: [
            'red',
            'blue',
            'green',
            'orange',
            'black',
            'coral',
            'cyan',
            'DarkGray',
            'DarkOrange',
            'violet',
            'yellow',
            'lime',
            'teal',
          ],
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
      title: {
        display: true,
        text: 'Disease cooccurence',
      },
    },
  });
  chart6.update();
};

const bootstrapGraphs = () => {
  graphRequest(
    'Overview',
    $(`#OverviewDateDropdown`)
      .find('.btn')
      .text(),
    $(`#OverviewDiseaseDropdown`)
      .find('.btn')
      .text(),
    '',
  );
  graphRequest(
    'Breakdown',
    $(`#BreakdownDateDropdown`)
      .find('.btn')
      .text(),
    $(`#BreakdownDiseaseDropdown`)
      .find('.btn')
      .text(),
    $(`#BreakdownVillageDropdown`)
      .find('.btn')
      .text(),
  );
  graphRequest(
    'Map',
    $(`#MapDateDropdown`)
      .find('.btn')
      .text(),
    $(`#MapDiseaseDropdown`)
      .find('.btn')
      .text(),
    '',
  );
  graphRequest(
    'List',
    $(`#ListDateDropdown`)
      .find('.btn')
      .text(),
    $(`#ListDiseaseDropdown`)
      .find('.btn')
      .text(),
    '',
  );
};

const bootstrapMapInit = () => {
  // add the OpenStreetMap tiles
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution:
      '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap contributors</a>',
  }).addTo(map);
  //
  // // show the scale bar on the lower left corner
  L.control.scale().addTo(map);
};

const bootstrapMap = (coords, label) => {
  // // show a marker on the map
  for (var i = 0; i < coords.length; i++) {
    L.marker(coords[i])
      .bindPopup(label[i])
      .addTo(map);
  }

  map.invalidateSize();
};

socketIO.on('data', _d => {
  // New data triggered here. _d will be empty.
  bootstrapGraphs();
});

bootstrapElements();
bootstrapMapInit();
adminWarning();
