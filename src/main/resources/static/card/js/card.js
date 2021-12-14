/// init my cards with @parent
/// need to add 'card' class to each card
/// and add 'content' class to each content in each card
var MyCards = {
    /// init
    /// parent: a node which contains '.card'
    /// replaceANode: true - replace <a> node class name to make it like a button
    init: function (parent, replaceANode) {
        parent.className = "container d-flex flex-wrap";
        let cards = parent.querySelectorAll(".card");
        cards.forEach(function (card) {
            card.className = `card overflow-hidden m-5 shadow d-flex justify-content-center`;
        });
        parent.querySelectorAll(".card .content").forEach(function (content) {
            content.className = `content text-center p-4 text-white`;
        });
        if (replaceANode) {
            parent.querySelectorAll(".card .content a").forEach(function (a) {
                a.className = `btn btn-secondary bg-white text-dark text-decoration-none font-weight-bold shadow m-3`;
            });
        }
        VanillaTilt.init(cards, {
            max: 25,
            speed: 400
        });
    }
};