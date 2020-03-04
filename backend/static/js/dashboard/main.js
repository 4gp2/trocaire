const patient_list_container = document.getElementById('plist');
const patient_template = document.querySelector('template').content.cloneNode(true);
const patient_details_container = document.getElementById('pdetails');
var database_response = null;

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
};

document.getElementById('searchB').addEventListener('click', async e => {
  e.preventDefault();
  patient_list_container.innerHTML = "";

  const token = await firebase.auth().currentUser.getIdToken(true);
  database_response = await axios.post('/api/patient', {
    token,
    lastName: document.getElementById('lName').value,
    firstName: document.getElementById('fName').value,
    year: document.getElementById('year').value.toString(),
    month: document.getElementById('month').value.toString(),
    day: document.getElementById('day').value.toString(),
  });

  if(database_response.data.error) {
    patient_list_container.appendChild(patient_template.getElementById('noPatientFound'));
    console.log("no search found");
    return;
  }

  patient_template.getElementById('pNameFound').textContent = database_response.data.firstName + " " + database_response.data.lastName;
  patient_list_container.appendChild(patient_template.getElementById('pNameFound'));
});

patient_template.getElementById('pNameFound').addEventListener('click', async e => {
  e.preventDefault();
  patient_details_container.appendChild(document.createElement('form'));
  database_response.data.forEach(element => {
    patient_template.getElementById('pContentRow').querySelector('label').textContent = element.key;
    patient_template.getElementById('pContentRow').querySelector('input').setAttribute('value', element.value);
    patient_details_container.querySelector('form').appendChild(patient_template.getElementById('pContentRow'));
  });
});

// console.log(axios.get("https://jsonplaceholder.typicode.com/users"));


initFirebase();
bootstrapElements();
