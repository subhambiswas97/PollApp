import React,{Component} from 'react'
import axios from 'axios'

class LoginPage extends Component {
    constructor(props) {
        super(props)
        this.state = {
            email : '',
            password : ''
        }
    }
    emailHandler = (event) => {
        this.setState({
            email : event.target.value
        })
    }

    passwordHandler = (event) => {
        this.setState({
            password : event.target.value
        })
    }

    submitHandler = (event) => {
        //alert(`${this.state.email} \n ${this.state.password}`)
        axios.post('/GetToken',this.state)
        .then(response => {
            console.log(response);
            if(response.data.status == 200 ) {
                this.props.setMethod(response.data.token)
            }
        })
        .catch(error => {console.log(error)});
        event.preventDefault()
    }
    render() {
        return (
            <div>
                <form onSubmit={this.submitHandler}>
                    <h2> Log In</h2>
                    <br/>
                    <input type='email' value={this.state.email} onChange={this.emailHandler} placeholder='Email' required></input>
                    <br/>
                    <input type='password' value={this.state.password} onChange={this.passwordHandler} placeholder='Password' required></input>
                    <br/>
                    <button type='submit'>Log In</button>
                </form>
            </div>
        )
    }
}

export default LoginPage;