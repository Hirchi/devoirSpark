const express = require("express");
const app = express()
const expressWs = require('express-ws')(app);
const eventsWs = expressWs.getWss('/');
const { Kafka } = require('kafkajs');

const kafka = new Kafka({
    brokers: ["localhost:9092"]
});

const consumer = kafka.consumer({ groupId: 'test' })

app.use(express.static('public'));

let data = []

app.ws('/alerts', function (ws, req) {
    sendData();
});

const sendData = () => {
    eventsWs.clients.forEach((ws) => {
        ws.send(JSON.stringify(data));
    });

    data = []
}

const run = async () => {

    await consumer.connect();
    await consumer.subscribe({ topic: "alertReportTopic", fromBeginning: false });

    await consumer.run({
        eachBatch: async ({ batch }) => {
            batch.messages.forEach((msg) => {
                const json = msg.value.toString();
                console.log(json)
                console.log(JSON.parse(json))
                data.push(json)
            });

            sendData();
        }
    })
};

run().catch(console.error);

app.listen(8000)