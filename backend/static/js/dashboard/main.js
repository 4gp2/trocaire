let dbRes;

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
};

const bootstrapGraphs = () => {
  new Chart(document.getElementById('chart1'), {
    type: 'bar',
    data: {
      labels: [
        '<18',
        '18-30',
        '30-50',
        '50-65',
        '65+',

      ],
      datasets: [
        {
          data: [100, 340, 645, 432, 320],
          lineTension: 0,
          backgroundColor:  ["red", "blue", "green","orange", "black"],
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
      labels: [
        'Village A',
        'Village B',
        'Village C',
        'Village D',
      ],
      datasets: [
        {
          data: [15339, 21345, 18483, 24003],
          lineTension: 0,
          backgroundColor:["red", "blue", "green","orange", "black"],
          borderColor:  'transparent',
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
      labels: [
        'Male',
        'Female',
      ],
      datasets: [
        {
          data: [38, 62],
          lineTension: 0,
          backgroundColor: [ "blue", "orange"],
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

  // initialize Leaflet
      var map = L.map('map').setView({lon: 0, lat: 0}, 2);

      // add the OpenStreetMap tiles
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap contributors</a>'
      }).addTo(map);
      //
      // // show the scale bar on the lower left corner
      L.control.scale().addTo(map);

      // // show a marker on the map
      L.marker({lon: 0, lat: 0}).bindPopup('The center of the world').addTo(map);

      map.invalidateSize();
};

bootstrapElements();
bootstrapGraphs();
bootstrapMap();
