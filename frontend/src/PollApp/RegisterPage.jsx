import React,{Component} from 'react'
//import createFormPost from './createFormPost'
import axios from 'axios';

class RegisterPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            firstName :'',
            lastName : '',
            email : '',
            password : '',
            confirmPassword :'',
            
                firstnameError : '',
                lastnameError : '',
                emailError : '',
                passwordError : '',
                confirmPasswordError :''
            
            
        }
    }



    fNameHandler = (event) => {
        this.setState({
            firstName : event.target.value,
            firstnameError : event.target.value.length<5 ? 'First Name should be 5 characters long.' : ''
        })
        
    }
    lNameHandler = (event) => {
        this.setState({
            lastName : event.target.value,
            lastnameError : event.target.value.length<5 ? 'Last Name should be 5 characters long.' : ''
        })
    }
    emailHandler = (event) => {
        let emailRegExp =  RegExp(/^[\w-_\.+]*[\w-_\.]\@([\w]+\.)+[\w]+[\w]$/i);
        this.setState({
            email : event.target.value,
            emailError : emailRegExp.test(event.target.value) ? '' : 'Email Incorrect Format'
        })
    }
    passwordHandler = (event) => {
        this.setState({
            password : event.target.value,
            passwordError : event.target.value.length<5 ? 'Password should be 8 characters long.' : ''
        })
    }
    confirmPasswordHandler = (event) => {
        this.setState({
            confirmPassword : event.target.value,
            confirmPasswordError : (this.state.password.valueOf() != event.target.value) ? 'Confirm Password and Password not same.' : ''
        })
    }

    
    submitHandler = (event) => {
        
        
        if(this.state.firstnameError.length>0)
            alert(this.state.firstnameError)
        else if(this.state.lastnameError.length>0)
            alert(this.state.lastnameError)
        else if(this.state.emailError.length>0)
            alert(this.state.emailError)
        else if(this.state.passwordError.length>0)
            alert(this.state.passwordError)
        else if(this.state.confirmPasswordError.length>0)
            alert(this.state.confirmPasswordError)
        else {
            //event.preventDefault()
            //axios.post('https://jsonplaceholder.typicode.com/posts',this.state)
            axios.post('http://localhost:8080/users',this.state)
            .then(response => {console.log(response)})
            .catch(error => {console.log(error)});
            console.log(`${this.state.firstName} \n` + 
            `${this.state.lastName} \n` + 
            `${this.state.email} \n` + 
            `${this.state.password} \n` + 
            `${this.state.confirmPassword}  `)
            //createFormPost(this.state)

        }
        event.preventDefault()
    }
    render() {
        return (
            <div>
                <div className='registerDiv'>
                    <h2>Sign Up</h2>
                    <form onSubmit={this.submitHandler}>
                        <input type='text' placeholder='First Name' value={this.state.firstName} onChange={this.fNameHandler} required></input>
                        <br/>
                        <input type='text' placeholder='Last Name' value={this.state.lastName} onChange={this.lNameHandler} required></input>
                        <br/>
                        <input type='email' placeholder='Email' value={this.state.email} onChange={this.emailHandler} required></input>
                        <br/>
                        <input type='password' placeholder='Password' value={this.state.password} onChange={this.passwordHandler} required></input>
                        <br/>
                        <input type='password' placeholder='Confirm Password' value={this.state.confirmPassword} onChange={this.confirmPasswordHandler} required></input>
                        <br/>
                        <button type='submit'>Submit</button>
                    </form>

                </div>
            </div>
        )
    }
}

export default RegisterPage