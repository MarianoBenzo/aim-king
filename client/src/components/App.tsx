import React, {Suspense, lazy} from "react";
import ReactDOM from "react-dom";
import {BrowserRouter, Route, Switch} from "react-router-dom";

const Game = lazy(() => import("components/game/GameWrapper"));

const App = () => (
        <BrowserRouter>
            <Suspense fallback={null}>
                <Switch>
                    <Route exact path="/" component={Game}/>
                </Switch>
            </Suspense>
        </BrowserRouter>
);

ReactDOM.render(<App/>, document.getElementById("app"));
