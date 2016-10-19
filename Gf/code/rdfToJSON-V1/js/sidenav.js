function openNav() {
    if (document.getElementById("mySidenav").style.width != "250px")
        document.getElementById("mySidenav").style.width = "250px";
    else
        document.getElementById("mySidenav").style.width = "0";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}