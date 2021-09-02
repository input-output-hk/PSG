function streamHandler (call, onData, onEnd, onError, onStatus) {
    call.on('data', onData);
    call.on('end', onEnd);
    call.on('error', onError);
    call.on('status', onStatus);
};

function loggingStreamHandler (call) {
    streamHandler(
        call,
        (data) => console.log('data = ' + data),
        () => console.log('end'),
        (e) => console.error(e),
        (status) => console.log('status = ' + status.code)
    );
};

module.exports = { streamHandler, loggingStreamHandler }
