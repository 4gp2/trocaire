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

initFirebase();
bootstrapElements();
