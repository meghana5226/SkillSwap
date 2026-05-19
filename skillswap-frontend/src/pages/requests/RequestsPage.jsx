import {useEffect,useState} from "react";

import RequestCard
from "../../components/requests/RequestCard";

import {
getRequests,
acceptRequest,
rejectRequest
}
from "../../api/requestApi";

const RequestsPage=()=>{

const [requests,setRequests]=
useState([]);

const loadRequests=
async()=>{

const data=
await getRequests();

setRequests(data);

};

useEffect(()=>{

loadRequests();

},[]);


return(

<div className="requests-page">

<h1>

Requests

</h1>

{

requests.map(

(item)=>(

<RequestCard

key={item.id}

name={item.senderName}

skill={item.skillName}

status={item.status}

onAccept={
async()=>{

await acceptRequest(
item.id
);

loadRequests();

}
}

onReject={
async()=>{

await rejectRequest(
item.id
);

loadRequests();

}
}

/>

)

)

}

</div>

);

};

export default RequestsPage;