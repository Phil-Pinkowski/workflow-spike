exports.handler = async (event, context) => {
  console.log("Did a thing!");
  console.log(`EVENT:\n${JSON.stringify(event, null, 2)}`);
  return {
    ...event,
    scheduleForDate: new Date(new Date().getTime() + 10 * 1000).toISOString()
  };
};
