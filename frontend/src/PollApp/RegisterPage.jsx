import React,{Component} from 'react'
//import createFormPost from './createFormPost'
import axios from 'axios';

class RegisterPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            firstname :'',
            lastname : '',
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


    // validateForm() {
    //     let valid = true;
    //     Object.values(errors).forEach(
    //         (value) => value.length > 0 && (valid = false)
    //     )
    //     return valid;
    // }

    fNameHandler = (event) => {
        this.setState({
            firstname : event.target.value,
            firstnameError : event.target.value.length<5 ? 'First Name should be 5 characters long.' : ''
        })
        
    }
    lNameHandler = (event) => {
        this.setState({
            lastname : event.target.value,
            lastnameError : event.target.value.length<5 ? 'Last Name should be 5 characters long.' : ''
        })
    }
    emailHandler = (event) => {
        let emailRegExp =  RegExp(/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i);
        this.setState({
            email : event.target.value,
            emailError : emailRegExp.test(event.target.value) ? 'Email Incorrect Format' : ''
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
        
        // let emailRegExp =  RegExp(/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i);

        // const {name,value} = event.target;
        // let errors = this.state.errors;
        // switch(name) {
        //     case 'firstname' : 
        //     this.setState({errors.firstname = value.length<5 ? 'First Name should be 5 characters long.' : ''})
        //     break;
        //     case 'lastname' : 
        //     this.setState({errors.lastname = value.length<5 ? 'Last Name should be 5 characters long.' : ''})
        //     break;
        //     case 'email' : 
        //     this.setState({errors.email = emailRegExp.test(value) ? 'Email Incorrect Format' : ''})
        //     break;
        //     case 'password' : 
        //     this.setState({errors.password = value.length<8 ? 'Password should be 8 characters long.' : ''})
        //     break;
        //     case 'confirmPassword' : 
        //     this.setState({errors.confirmPassword = value != event.target.password.value ? 'Password and Conirm Password must be same.' : ''})
        //     break;
        //     default:
        //     break;

        // }

        // if(this.validateForm(this.state.errors)) {
        //     alert(`${this.state.firstname} \n` + 
        //     `${this.state.lastname} \n` + 
        //     `${this.state.email} \n` + 
        //     `${this.state.password} \n` + 
        //     `${this.state.confirmPassword}  `)
        // }
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
            axios.post('localhost:8080/Users',this.state)
            .then(response => {console.log(response)})
            .catch(error => {console.log(error)});
            console.log(`${this.state.firstname} \n` + 
            `${this.state.lastname} \n` + 
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
                        <input type='text' placeholder='First Name' value={this.state.firstname} onChange={this.fNameHandler} required></input>
                        <br/>
                        <input type='text' placeholder='Last Name' value={this.state.lastname} onChange={this.lNameHandler} required></input>
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