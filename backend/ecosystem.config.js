module.exports = {
  apps: [
    {
      name: 'trocaire',
      script: '/home/trocaire/.yarn/bin/pm2',
      args: 'run start:prod',
    },
  ],
};
