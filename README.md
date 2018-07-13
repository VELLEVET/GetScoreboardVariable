This plugin can be used for getting some Scoreboard values.
To use this, you have to to GET-request with some parameters: act, specific additional info and, probably, pass, if you're trying to get blacklisted objectives.
1. Unitary value:
    1) act = value
    2) vname = objective name
    3) pname = player name
    4) pass = passphrase (necessary only if you try to get blacklisted for values objective)
2. Rating:
    1) act = top
    2) vname = objective name
    3) size = depth of rating
    4) min = minimal value (not necessary)
    5) pass = passphrase (necessary only if you try to get blacklisted for tops objective)

Universal parameters:
	1) string:
		Type "true", if you want to get a response without line breaking. Type something another or nothing to get a pretty response.