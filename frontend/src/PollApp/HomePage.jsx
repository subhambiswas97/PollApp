import React,{Component} from 'react';
import UserDetails from './UserDetails';
import axios from 'axios';

class HomePage extends Component {
    constructor(props) {
        super(props)
        this.state = {
            token : this.props.token,
            data : {}
        }
        this.getDetails()
    }
    getDetails() {
        
        axios.post('/getuser',this.state)
        .then(response => {
            console.log(response.data);
            //return response.data;
            this.setState({
                data : response.data
            })
        }).catch(error => { console.log(error)})
    }
    render() {
        //this.getDetails()
        let user = this.state.data
        return (
            <div>
                <h1>Hello {this.props.token}</h1> <br/>
                Name : {user.firstName} + {user.lastName} <br/>
                Email : {user.email} <br/>
                  {/* <UserDetails token={this.props.token}></UserDetails>   */}
            </div>
        )
    }
}

export default HomePage;