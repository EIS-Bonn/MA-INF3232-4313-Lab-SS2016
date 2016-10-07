import React from 'react';

import PageTitle from '../components/layout/PageTitle';

const Documentation = () => {

  return (
    <div id='page-content-wrapper'>
      <div class='container-fluid'>
        <div class='row'>
          <div class='documentation col-lg-12'>
            <PageTitle pageTitle={'Documentation'} />

            <div class='user-manual'>
              <h2>User Manual</h2>
              <p>This guide would demostrate how to:</p>
              <ol>
                <li>Browse general information about libraries.</li>
                <li>Browse different categories.</li>
                <li>Browse experiment results.</li>
                <li>Select and apply filters.</li>
              </ol>
              <section>
              <div>
                <h3>1. Browse general information about libraries</h3>
                <p>Select the “All” tab from the tabs list.</p>
                <p>You can see general information about all studied JS libraries. This information contains:</p>
                <ul>
                  <li>License type</li>
                  <li>Available Documentation (Docs)</li>
                  <li>Open source status (OSS)</li>
                  <li>Github Stars</li>
                  <li>W3C Cerfitication (W3C Cerf)</li>
                  <li>Latest Update</li>
                  <li>Library Link</li>
                </ul>
              </div>
              <img src="./src/img/All.jpg" alt="rdf-js" height="50%" width="50%"></img>
              </section>
              <section>
              <div>
              <h3>2. Browse different categories</h3>
              <p>There are 4 main categories, which were studied during this project and specified by their characteristics:
              (Detailed information on each category specific features below)</p>
              <ul>
                <li>Parsing</li>
                <li>SPARQL/Query</li>
                <li>Data storage</li>
                <li>UI data binding</li>
              </ul>

              <h4>> Parsing</h4>
              <ul>
                <li>Supported Media type</li>
                <li>Operating Environment</li>
                <li>Interface used for operations</li>
              </ul>
              <h4>> SPARQL/Query</h4>
              <ul>
                <li>Filtering used for retrieving data</li>
                <li>Supported query Language</li>
              </ul>
              <h4>> Data Storage</h4>
              <ul>
                <li>Data storage method</li>
                <li>Indexing to make a structure for storing data</li>
              </ul>
              <h4>> UI data binding</h4>
              <ul>
                <li>Libraries and frameworks integrated with</li>
                <li>Data binding approaches</li>
              </ul>
            </div>
            <img src="./src/img/Parsing.jpg" alt="rdf-js" height="50%" width="50%"></img>
            </section>
              <section>
                <div>
              <h3>3. Browse experiment results</h3>
              <p>Select the “Experiment results” tab from the tabs list.</p>
              <p>Adjust the libraries with regard to desired options:</p>
              <ul>
                <li>2 different Sizes of datasets (100 k, 1m).</li>
                <li>3 different Types of queries (Linear, Complex, Snowflake)</li>
              </ul>
              </div>
              <img src="./src/img/Experiment.jpg" alt="rdf-js" height="50%" width="50%"></img>
              </section>
              <section>
                  <div>
                    <h3>4. Select and apply filters</h3>
                    <p>Filtering can be applied on each category of libraries mentioned above.</p>
                    <ul>
                      <li>Choose your desired libraries from the tabs list, and a related features set box on the left sidebar would be activated</li>
                      <li>Pick a feature and select a value from suggested options for dropdown or simply enable/disable a checkbox</li>
                      <li>Click Apply button to active the filters</li>
                      <li>When the features set box of a category is active, click on Reset button in the right top corner to reset the table’s contents to default values</li>
                    </ul>
                  </div>
                <img src="./src/img/Apply.jpg" alt="rdf-js" height="20%" width="20%"></img>
                <img src="./src/img/Filtering.jpg" alt="rdf-js" height="20%" width="20%"></img>
              </section>

            </div>

            <div class='developer-notes'>
              <h2>Developers notes</h2>
              <h3>Setup</h3>

                <p>Datasets used for evaluation: <a rel='noopener noreferrer' target='_blank' hre='http://dsg.uwaterloo.ca/watdiv'>Waterloo</a>. A tool to generate sample datasets, additionaly may also use the datasets provided for this evaluation.</p>
                <p>Environment: MacBook Pro(Late 2013, 2 GHz Intel Core i7, 8 GB 1600 MHz DDR3) + Apache web server.</p>
                <p>Each library has a separate *.js file. Simply invoke the functions to run the experiment. There is a timer at the beginning of each file, and it stops at the end of processing. The duration of this period is the metric used for evaluation.</p>
                <p>In order to have measurable data 3 different query results per library were collected; one simple and two complex queries.</p>

              <h3>Recommendations:</h3>
              <p>It’ s not easy to decide which library is the best for running SPARQL queries on the client side, but to our knowledge <a rel='noopener noreferrer' target='_blank' href="https://github.com/linkeddata/rdflib.js">rdflib</a> performed better, and it's also stable. Moreover, there are frequent updates on it.</p>
              <p>There is no need to say, but no one is willing to have such intensive tasks on the client side. We have evaluate over 10, 000 records in our experiments, which is almost non sense in the client side. All libraries are performing good in a reasonable amount of records on the client side ~100 records.</p>

              <h3>Results:</h3>
              <a el='noopener noreferrer' target='_blank' href="https://gist.github.com/txwkx/a59c78d522cfbe46d60e7d66bd04a23f"> > Link to a Gist</a>
            </div>

          </div>
        </div>
      </div>
    </div>
    );

  };

  export default Documentation;
