import Button from "../ui/Button";

const RequestCard=({

name,
skill,
status,
onAccept,
onReject

})=>{

return(

<div className="request-card">

<div>

<h3>

{name}

</h3>

<p>

{skill}

</p>

</div>

<div>

<span
className={`status ${status}`}
>

{status}

</span>

</div>

{

status==="PENDING" && (

<div
className="request-actions"
>

<Button
onClick={onAccept}
>

Accept

</Button>

<Button
variant="secondary"
onClick={onReject}
>

Reject

</Button>

</div>

)

}

</div>

);

};

export default RequestCard;