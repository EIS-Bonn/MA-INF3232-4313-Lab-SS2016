import React from 'react';

import PageTitle from '../components/layout/PageTitle';

const About = () => {

  return (
    <div id='page-content-wrapper'>
      <div class='container-fluid'>
        <div class='row'>
          <div class='about col-lg-12'>
            <PageTitle pageTitle={'Project'} />
            <section>
              <p>This project was developed as a part of Semantic Web and Enterprise Information Systems modules.</p>
              <p>University of Bonn @ Summer Semester 2016.</p>
              <a class='btn btn-social btn-github' href='https://github.com/txwkx/RDFJS4U' rel='noopener noreferrer' target='_blank'>
                <span class='fa fa-github'></span> Github repository
                </a>
              </section>
              <section>
                <div class='copyright'>
                  <h3>Copyright</h3>
                  <p>The MIT License (MIT)</p>

                  <p>Copyright (c) 2016</p>

                  <p>Permission is hereby granted, free of charge, to any person obtaining a copy
                    of this software and associated documentation files (the "Software"), to deal
                    in the Software without restriction, including without limitation the rights
                    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
                    copies of the Software, and to permit persons to whom the Software is
                    furnished to do so, subject to the following conditions:</p>

                  <p>The above copyright notice and this permission notice shall be included in all
                    copies or substantial portions of the Software.</p>

                  <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
                    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
                    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
                    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
                    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
                    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
                    SOFTWARE.</p>
                </div>
              </section>
            </div>
          </div>
        </div>
      </div>
    );

  };

  export default About;
