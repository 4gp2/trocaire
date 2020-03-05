let dbRes;

const initFirebase = () => {
  firebase.initializeApp({
    apiKey: 'AIzaSyCizCQ2QIyUZ6ONcHwmyGmHPyB6jwQji2c',
    authDomain: 'trocaire-4gp2.firebaseapp.com',
    databaseURL: 'https://trocaire-4gp2.firebaseio.com',
    projectId: 'trocaire-4gp2',
    storageBucket: 'trocaire-4gp2.appspot.com',
    messagingSenderId: '772364666256',
    appId: '1:772364666256:web:87b0169305869cd4195f15',
  });
  firebase.auth().setPersistence(firebase.auth.Auth.Persistence.SESSION);
};

const bootstrapElements = () => {
  document
    .getElementById('year')
    .setAttribute('max', new Date().getFullYear().toString());

  document.getElementById('logout').addEventListener('click', async e => {
    e.preventDefault();
    await firebase.auth().signOut();
    await axios.get('/logout');
    window.location.href = window.location.origin;
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
};

initFirebase();
bootstrapElements();
