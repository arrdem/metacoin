;;;; Copyright 2013 Aviad Reich.
;;;; The use and distribution terms for this software are covered by
;;;; the Apache License, Version 2.0
;;;; (http://www.apache.org/licenses/LICENSE-2.0.txt), which can be
;;;; found in the file LICENSE at the root of this distribution. By
;;;; using this software in any fashion, you are agreeing to be bound
;;;; by the terms of this license. You must not remove this notice, or
;;;; any other, from this software

(ns metacoin.namecoin
  (:require [metacoin :refer [not-nil?]]))


(defmacro defrpc [& m0ar]
  `(metacoin/defrpc "namecoin" ~@m0ar))


(defrpc backupwallet
  "backupwallet <destination>

  Safely copies wallet.dat to destination, which can be a directory or a path with filename."
  [destination])


(defrpc buildmerkletree
  "buildmerkletree <obj>...

  build a merkle tree with the given hex-encoded objects."
  [objs])


(defrpc createrawtransaction
  "createrawtransaction [{\"txid\":txid,\"vout\":n},...] {address:amount,...}

  Create a transaction spending given inputs
  (array of objects containing transaction id and output number),
  sending to given address(es).  Returns hex-encoded raw transaction.
  Note that the transaction's inputs are not signed, and it is not
  stored in the wallet or transmitted to the network."
  [inputs? address-amount-maps])


(defrpc decoderawtransaction
  "decoderawtransaction <hex string>

  Return a JSON object representing the serialized, hex-encoded transaction."
  [hex-string])


(defrpc deletetransaction
  "deletetransaction <txid>

  Normally used when a transaction cannot be confirmed due to a double
  spend. Restart the program after executing this call."
  [txid])


(defrpc dumpprivkey
  "dumpprivkey <namecoinaddress>

  Reveals the private key corresponding to <namecoinaddress>."
  [namecoinaddress])


(defrpc encryptwallet
  "encryptwallet <passphrase>

  Encrypts the wallet with <passphrase>."
  [passphrase])


(defrpc getaccount
  "getaccount <namecoinaddress>

  Returns the account associated with the given address."
  [namecoinaddress])


(defrpc getaccountaddress
  "getaccountaddress <account>

  Returns the current Namecoin address for receiving payments to this account."
  [account])


(defrpc getaddressesbyaccount
  "getaddressesbyaccount <account>

  Returns the list of addresses for the given account."
  [account])


(defrpc getauxblock
  "getauxblock [<hash> <auxpow>]

  create a new blockIf <hash>, <auxpow> is not specified, returns a
  new block hash. If <hash>, <auxpow> is specified, tries to solve the
  block based on the aux proof of work and returns true if it was
  successful."
  [hash auxpow])


(defrpc getbalance
  "getbalance [account] [minconf=1]

  If [account] is not specified, returns the server's total available
  balance.  If [account] is specified, returns the balance in the
  account."
  [account? minconf?])


(defrpc getblock
  "getblock hash

  Dumps the block with specified hash."
  [hash])


(defrpc getblockbycount
  "getblockbycount height

  Dumps the block existing at specified height."
  [height])


(defrpc getblockcount
  "getblockcount
  Returns the number of blocks in the longest block chain."
  [])


(defrpc getblockhash
  "getblockhash <index>

  Returns hash of block in best-block-chain at <index>."
  [index])


(defrpc getblocknumber
  "getblocknumber

  Returns the block number of the latest block in the longest block chain."
  [])


(defrpc getconnectioncount
  "getconnectioncount

  Returns the number of connections to other nodes."
  [])


(defrpc getdifficulty
  "getdifficulty

  Returns the proof-of-work difficulty as a multiple of the minimum difficulty."
  [])


(defrpc getgenerate
  "getgenerate

  Returns true or false."
  [])


(defrpc gethashespersec
  "gethashespersec

  Returns a recent hashes per second performance measurement while generating."
  [])


(defrpc getinfo
  "getinfo

  Returns an object containing various state info."
  [])


(defrpc getmemorypool
  "getmemorypool [data]

  If [data] is not specified, returns data needed to construct a block to work on:
    \"version\" : block version
    \"previousblockhash\" : hash of current highest block
    \"transactions\" : contents of non-coinbase transactions that should be included in the next block
    \"coinbasevalue\" : maximum allowable input to coinbase transaction, including the generation award and transaction fees
    \"time\" : timestamp appropriate for next block
    \"bits\" : compressed target of next block
  If [data] is specified, tries to solve the block and returns true if it was successful."
  [data])


(defrpc getnewaddress
  "getnewaddress [account]

  Returns a new Namecoin address for receiving payments.  If [account]
  is specified (recommended), it is added to the address book so
  payments received with the address will be credited to [account]."
  [account])


(defrpc getrawmempool
  "getrawmempool

  Returns all transaction ids in memory pool."
  [])


(defrpc getrawtransaction
  "getrawtransaction <txid> [verbose=0]

  If verbose=0, returns a string that is serialized, hex-encoded data
  for <txid>.  If verbose is non-zero, returns an Object with
  information about <txid>."
  [txid verbose?])


(defrpc getreceivedbyaccount
  "getreceivedbyaccount <account> [minconf=1]

  Returns the total amount received by addresses with <account> in
  transactions with at least [minconf] confirmations."
  [account minconf?])


(defrpc getreceivedbyaddress
  "getreceivedbyaddress <namecoinaddress> [minconf=1]

  Returns the total amount received by <namecoinaddress> in
  transactions with at least [minconf] confirmations."
  [namecoinaddress minconf?])


(defrpc gettransaction
  "gettransaction <txid>

  Get detailed information about <txid>"
  [txid])


(defrpc getwork
  "getwork [data]

  If [data] is not specified, returns formatted hash data to work on:
    \"midstate\" : precomputed hash state after hashing the first half of the data
    \"data\" : block data
    \"hash1\" : formatted hash buffer for second hash
    \"target\" : little endian hash target
  If [data] is specified, tries to solve the block and returns true if it was successful."
  [data])


(defrpc getworkaux
  "getworkaux <aux>
   getworkaux '' <data>
   getworkaux 'submit' <data>
   getworkaux '' <data> <chain-index> <branch>*

  get work with auxiliary data in coinbase, for multichain mining
  <aux> is the merkle root of the auxiliary chain block hashes, concatenated with the aux chain merkle tree size and a nonce
  <chain-index> is the aux chain index in the aux chain merkle tree
  <branch> is the optional merkle branch of the aux chain
  If <data> is not specified, returns formatted hash data to work on:
    \"midstate\" : precomputed hash state after hashing the first half of the data
    \"data\" : block data
    \"hash1\" : formatted hash buffer for second hash
    \"target\" : little endian hash target
  If <data> is specified and 'submit', tries to solve the block for this (parent) chain and returns true if it was successful.If <data> is specified and empty first argument, returns the aux merkle root, with size and nonce.If <data> and <chain-index> are specified, creates an auxiliary proof of work for the chain specified and returns:
    \"aux\" : merkle root of auxiliary chain block hashes
    \"auxpow\" : aux proof of work to submit to aux chain."
  [op? data? chain-index? branch?])


(defrpc help
  "help [command]

  List commands, or get help for a command."
  [command?])


(defrpc importaddress
  "importaddress <namecoinaddress> [label] [rescan=true]

  Adds an address that can be watched as if it were in your wallet but cannot be used to spend."
  [namecoinaddress label rescan?])


(defrpc importprivkey
  "importprivkey <namecoinprivkey> [label] [rescan=true]

  Adds a private key (as returned by dumpprivkey) to your wallet."
  [namecoinprivkey label? rescan?])


(defrpc listaccounts
  "listaccounts [minconf=1]

  Returns Object that has account names as keys, account balances as values."
  [minconf?])


(defrpc listaddressgroupings
  "listaddressgroupings

  Lists groups of addresses which have had their common ownership made
  public by common use as inputs or as the resulting change in past
  transactions"
  [])


(defrpc listreceivedbyaccount
  "listreceivedbyaccount [minconf=1] [includeempty=false]


  [minconf] is the minimum number of confirmations before payments are
  included.  [includeempty] whether to include accounts that haven't
  received any payments.  Returns an array of objects containing:
  \"account\" : the account of the receiving addresses \"amount\" :
  total amount received by addresses with this account
  \"confirmations\" : number of confirmations of the most recent
  transaction included."
  [minconf? includeempty?])


(defrpc listreceivedbyaddress
  "listreceivedbyaddress [minconf=1] [includeempty=false]

  [minconf] is the minimum number of confirmations before payments are included.
  [includeempty] whether to include addresses that haven't received any payments.
  Returns an array of objects containing:
    \"address\" : receiving address
    \"account\" : the account of the receiving address
    \"amount\" : total amount received by the address
    \"confirmations\" : number of confirmations of the most recent transaction included."
  [minconf? includeempty?])


(defrpc listtransactions
  "listtransactions [account] [count=10] [from=0]

  Returns up to [count] most recent transactions skipping the first
  [from] transactions for account [account]."
  [account? count? from?])


(defrpc listunspent
  "listunspent [minconf=1] [maxconf=9999999]  [\"address\",...]

  Returns array of unspent transaction outputs with between minconf
  and maxconf (inclusive) confirmations.  Optionally filtered to only
  include txouts paid to specified addresses.  Results are an array of
  Objects, each of which has: {txid, vout, scriptPubKey, amount,
  confirmations}"
  [minconf? maxconf? addr-array?])


(defrpc move
  "move <fromaccount> <toaccount> <amount> [minconf=1] [comment]

  Move from one account in your wallet to another."
  [fromaccount toaccount amount minconf? comment?])


(defrpc sendfrom
  "sendfrom <fromaccount> <tonamecoinaddress> <amount> [minconf=1] [comment] [comment-to]

  <amount> is a real and is rounded to the nearest 0.01."
  [fromaccount tonamecoinaddress amount minconf? comment? comment-to?])


(defrpc sendmany
  "sendmany <fromaccount> {address:amount,...} [minconf=1] [comment]

  amounts are double-precision floating point numbers rounded to 0.01."
  [fromaccount addr-ammount-map minconf? comment?])


(defrpc sendrawtransaction
  "sendrawtransaction <hex string>

  Submits raw transaction (serialized, hex-encoded) to local node and network."
  [hex-string])


(defrpc sendtoaddress
  "sendtoaddress <namecoinaddress> <amount> [comment] [comment-to]

  <amount> is a real and is rounded to the nearest 0.01."
  [namecoinaddress amount comment? comment-to?])


(defrpc sendtoname
  "sendtoname <namecoinname> <amount> [comment] [comment-to]

  <amount> is a real and is rounded to the nearest 0.01."
  [namecoinname amount comment? comment-to?])


(defrpc setaccount
  "setaccount <namecoinaddress> <account>

  Sets the account associated with the given address."
  [namecoinaddress account])


(defrpc setgenerate
  "setgenerate <generate> [genproclimit]

  <generate> is true or false to turn generation on or off.
  Generation is limited to [genproclimit] processors, -1 is unlimited."
  [generate genproclimit?])


(defrpc setmininput
  "setmininput <amount>

  <amount> is a real and is rounded to the nearest 0.00000001"
  [amount])


(defrpc settxfee
  "settxfee <amount>

  <amount> is a real and is rounded to the nearest 0.00000001"
  [amount])


(defrpc signmessage
  "signmessage <namecoinaddress> <message>

  Sign a message with the private key of an address."
  [namecoinaddress message])


(defrpc signrawtransaction
  "signrawtransaction <hex string> [{\"txid\":txid,\"vout\":n,\"scriptPubKey\":hex},...] [<privatekey1>,...] [sighashtype=\"ALL\"]


  Sign inputs for raw transaction (serialized, hex-encoded).
  Second optional argument (may be null) is an array of previous transaction outputs that
  this transaction depends on but may not yet be in the block chain.
  Third optional argument (may be null) is an array of base58-encoded private
  keys that, if given, will be the only keys used to sign the transaction.
  Fourth optional argument is a string that is one of six values; ALL, NONE, SINGLE or
  ALL|ANYONECANPAY, NONE|ANYONECANPAY, SINGLE|ANYONECANPAY.
  Returns json object with keys:
    hex : raw transaction with signature(s) (hex-encoded string)
    complete : 1 if transaction has a complete set of signature (0 if not)"
  [hex-string prior-tx-outputs? private-keys? sighashtype?])


(defrpc stop
  "stop

  Stop namecoin server."
  [])


(defrpc validateaddress
  "validateaddress <namecoinaddress>

  Return information about <namecoinaddress>."
  [namecoinaddress])


(defrpc verifymessage
  "verifymessage <namecoinaddress> <signature> <message>

  Verify a signed message."
  [namecoinaddress signature message])


(defrpc name_clean 
  "name_clean

  Clean unsatisfiable transactions from the wallet - including
  name_update on an already taken name"
  [])


(defrpc name_debug 
  "name_debug

  Dump pending transactions id in the debug file."
  [])


(defrpc name_debug1 
  "name_debug1 <name>

  Dump name blocks number and transactions id in the debug file."
  [name])


(defrpc name_filter 
  "name_filter [regexp] [maxage=36000] [from=0] [nb=0] [stat]

  scan and filter names."
  [regexp maxage? from? nb? stat?])


(defrpc name_firstupdate 
  "name_firstupdate <name> <rand> [<tx>] <value>

  Perform a first update after a name_new reservation.\nNote that the
  first update will go into a block 12 blocks after the name_new, at
  the soonest."
  [name rand tx? value?])


(defrpc name_history 
  "name_history <name>\nList all name values of a name.\n"
  [name])


(defrpc name_list 
  "name_list [<name>]\nlist my own names"
  [name?])


(defrpc name_new 
  "name_new <name>"
  [name])


(defrpc name_scan 
  "name_scan [<start-name>] [<max-returned>]

  scan all names, starting at start-name and returning a maximum
  number of entries (default 500)."
  [start-name? max-returned?])


(defrpc name_show 
  "name_show <name>

  Show values of a name."
  [name])


(defrpc name_update 
  "name_update <name> <value> [<toaddress>]

  Update and possibly transfer a name."
  [name value toaddress?])
