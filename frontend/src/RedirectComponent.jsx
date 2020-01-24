import  App  from "./App";
import PublicPage from "./PollApp/PubicPage";
import React, { Component, Fragment } from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

class RedirectComponent extends Component {
    constructor(props) {
        super(props)
    }

    render() {
        return(
            <BrowserRouter>
            <Fragment>
                <Switch>
                    <Route exact path="/PublicPage/:id" component={PublicPage}></Route>
                    <Route exact path="/" component={App}></Route>
                    <Route render={() => (<Fragment><h1>Error 404</h1></Fragment>)}></Route>
                </Switch>
            </Fragment>
            </BrowserRouter>
        )
    }
}

export default RedirectComponent;