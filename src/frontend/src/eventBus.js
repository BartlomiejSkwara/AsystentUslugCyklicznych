import mitt from 'mitt';

const eventBus = mitt();
console.log('EventBus initialized:', eventBus);

export { eventBus };