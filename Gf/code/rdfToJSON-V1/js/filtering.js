function enableFilter() {
    if (document.getElementById('enable').checked) {

        document.getElementById('enableCountry').checked = false;
        disableAllCountries();

        document.getElementById('Company').checked = false;
        document.getElementById('Factory').checked = false;
        document.getElementById('Building').checked = false;
        document.getElementById('Machine').checked = false;
        document.getElementById('Others').checked = false;
        document.getElementById('Company').disabled = false;
        document.getElementById('Factory').disabled = false;
        document.getElementById('Building').disabled = false;
        document.getElementById('Machine').disabled = false;
        document.getElementById('Others').disabled = false;

        // remove all and show selected items on the map
        removeAllElementsFromMap();

    } else {

        disableAllFeatures();

        // show all the elements on the map
        removeAllElementsFromMap();
        processEverything(0, "");
    }
}


function disableAllFeatures() {
    document.getElementById('Company').checked = false;
    document.getElementById('Factory').checked = false;
    document.getElementById('Building').checked = false;
    document.getElementById('Machine').checked = false;
    document.getElementById('Others').checked = false;
    document.getElementById('Company').disabled = true;
    document.getElementById('Factory').disabled = true;
    document.getElementById('Building').disabled = true;
    document.getElementById('Machine').disabled = true;
    document.getElementById('Others').disabled = true;
}

function disableAllCountries() {
    document.getElementById('de').checked = false;
    document.getElementById('us').checked = false;
    document.getElementById('de').disabled = true;
    document.getElementById('us').disabled = true;
}


function enableCountryFilter() {
    if (document.getElementById('enableCountry').checked) {

        document.getElementById('enable').checked = false;
        disableAllFeatures();

        document.getElementById('de').checked = false;
        document.getElementById('us').checked = false;
        document.getElementById('de').disabled = false;
        document.getElementById('us').disabled = false;

        // remove all and show selected items on the map
        removeAllElementsFromMap();

    } else {

        disableAllCountries();

        // show all the elements on the map
        removeAllElementsFromMap();
        processEverything(0, "");
    }
}