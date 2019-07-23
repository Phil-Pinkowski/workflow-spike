exports.handler = async (event, context) => {
  console.log("Added to Exception Pot!");
  console.log(`EVENT:\n${JSON.stringify(event, null, 2)}`);
};
