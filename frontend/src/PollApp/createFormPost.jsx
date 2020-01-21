//import fetch from 'isomorphic-fetch';


function createFormPost(data) {
    return fetch('localhost:8080/Users', {
        method: 'POST',
        mode: 'CORS',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => {
        return res;
    }).catch(err => err);
}

export default createFormPost;