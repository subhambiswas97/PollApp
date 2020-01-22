import React,{Component} from 'react'
import axios from 'axios'

class UserDetails extends Component {

    constructor(props) {
        super(props)
        this.state = {
            token : this.props.token
        }
    }

    // getDetails = () => {
        
    //     axios.post('/GetUser',this.state)
    //     .then(response => {
    //         console.log(response);
    //         return response;
    //     }).catch(error => { console.log(error)})
    // }
    render() {
        // let user = this.getDetails();
        // console.log(user)
        return(
            <React.Fragment>
                Token : {this.state.token}
                {/* Name : {user.firstname} {user.lastname} <br/>
                Email : {user.email} <br/> */}
            </React.Fragment>
        )
    }
}

export default UserDetails;