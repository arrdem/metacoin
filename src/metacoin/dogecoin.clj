;;;; Copyright 2013 Aviad Reich.
;;;; The use and distribution terms for this software are covered by
;;;; the Apache License, Version 2.0
;;;; (http://www.apache.org/licenses/LICENSE-2.0.txt), which can be
;;;; found in the file LICENSE at the root of this distribution. By
;;;; using this software in any fashion, you are agreeing to be bound
;;;; by the terms of this license. You must not remove this notice, or
;;;; any other, from this software

(ns metacoin.dogecoin
  (:require [metacoin :refer [not-nil?]]))


(defmacro defrpc [& m0ar]
  `(metacoin/defrpc "dogecoin" ~@m0ar))


(defrpc addmultisigaddress
  "addmultisigaddress nrequired [\"key\",...] ( \"account\" )

  Add a nrequired-to-sign multisignature address to the wallet.
  Each key is a Dogecoin address or hex-encoded public key.
  If 'account' is specified, assign address to that account.

  Arguments:

    1. nrequired (numeric, required) The number of required signatures
       out of the n keys or addresses.

    2. \"keysobject\" (string, required) A JSON array of Dogecoin
       addresses or hex-encoded public keys
       [ \"address\" (string) Dogecoin address or hex-encoded public key .., ]

    3. \"account\" (string, optional) An account to assign the addresses to.

  Result:
    \"dogecoinaddress\" (string) A Dogecoin address associated with the keys."
  [nrequired keys account?]
  {:pre [(integer? nrequired)
         (vector? keys)
         (every? string? keys)]})


(defrpc addnode
  "addnode \"node\" \"add|remove|onetry\"

  Attempts add or remove a node from the addnode list.
  Or try a connection to a node once.

  Arguments:

    1. \"node\" (string, required) The node (see getpeerinfo for
       nodes)

    2. \"command\" (string, required) 'add' to add a node to the list,
       'remove' to remove a node from the list, 'onetry' to try a
        connection to the node once."
  [node add-remove-onetry]
  {:pre [(string? node)
         (string? add-remove-onetry)]})


(defrpc backupwallet
  "backupwallet \"destination\"

  Safely copies wallet.dat to destination, which can be a directory or
  a path with filename.

  Arguments:

    1. \"destination\" (string) The destination directory or file."
  [destination]
  {:pre [(string? destination)]})


(defrpc createmultisig
  "createmultisig nrequired [\"key\",...]

  Creates a multi-signature address with n signature of m keys required.
  It returns a json object with the address and redeemScript.

  Arguments:

    1. nrequired (numeric, required) The number of required signatures
       out of the n keys or addresses.

    2. \"keys\" (string, required) A json array of keys which are
       dogecoin addresses or hex-encoded public keys

     [
       \"key\" (string) dogecoin address or hex-encoded public key
       ,...
     ]

  Result:
    {
      \"address\":\"multisigaddress\",  (string) The value of the new multisig address.
      \"redeemScript\":\"script\"       (string) The string value of the hex-encoded redemption script.
    }"
  [nrequired keys]
  {:pre [(integer? nrequired)
         (vector? keys)
         (every? string? keys)]})


(defrpc createrawtransaction
  "createrawtransaction [{\"txid\":\"id\",\"vout\":n},...] {\"address\":amount,...}

  Create a transaction spending the given inputs and sending to the
  given addresses.  Returns hex-encoded raw transaction.  Note that
  the transaction's inputs are not signed, and it is not stored in the
  wallet or transmitted to the network.

  Arguments:

    1. \"transactions\" (string, required) A json array of json
       objects

       [
         {
           \"txid\":\"id\",  (string, required) The transaction id
           \"vout\":n        (numeric, required) The output number
         }
         ,...
       ]

    2. \"addresses\" (string, required) a json object with addresses
       as keys and amounts as values

       {
         \"address\": x.xxx  (numeric, required) The key is the Dogecoin address, the value is the doge amount
         ,...
       }

  Result:
    \"transaction\" (string) hex string of the transaction."

  [txids-map addrs-amounts-map]
  {:pre [(map? txids-map)
         (map? addrs-amounts-map)]})


(defrpc decoderawtransaction
  "decoderawtransaction \"hexstring\"

  Return a JSON object representing the serialized, hex-encoded
  transaction.

  Arguments:

    1. \"txid\" (string, required) The transaction hex string

  Result:
    {
      \"hex\" : \"data\",       (string) The serialized, hex-encoded data for 'txid'
      \"txid\" : \"id\",        (string) The transaction ID (same as provided)
      \"version\" : n,          (numeric) The version
      \"locktime\" : ttt,       (numeric) The lock time
      \"vin\" : [               (array of json objects)
         {
           \"txid\": \"id\",    (string) The transaction ID
           \"vout\": n,         (numeric) The output number
           \"scriptSig\": {     (json object) The script
             \"asm\": \"asm\",  (string) asm
             \"hex\": \"hex\"   (string) hex
           },
           \"sequence\": n      (numeric) The script sequence number
         }
         ,...
      ],
      \"vout\" : [              (array of json objects)
         {
           \"value\" : x.xxx,            (numeric) The value in doge
           \"n\" : n,                    (numeric) index
           \"scriptPubKey\" : {          (json object)
             \"asm\" : \"asm\",          (string) the asm
             \"hex\" : \"hex\",          (string) the hex
             \"reqSigs\" : n,            (numeric) The required sigs
             \"type\" : \"pubkeyhash\",  (string) The type, eg 'pubkeyhash'
             \"addresses\" : [           (json array of string)
               \"12tvKAXCxZjSmdNbao16dKXC8tRWfcF5oc\"   (string) Dogecoin address
               ,...
             ]
           }
         }
         ,...
      ],
      \"blockhash\" : \"hash\",   (string) the block hash
      \"confirmations\" : n,      (numeric) The confirmations
      \"time\" : ttt,             (numeric) The transaction time in seconds since epoch (Jan 1 1970 GMT)
      \"blocktime\" : ttt         (numeric) The block time in seconds since epoch (Jan 1 1970 GMT)
    }"
  [hex-string]
  {:pre (string? hex-string)})


(defrpc decodescript
  "decodescript \"hex\"

  Decode a hex-encoded script.

  Arguments:

    1. \"hex\" (string) the hex encoded script

  Result:
    {
      \"asm\":\"asm\",   (string) Script public key
      \"hex\":\"hex\",   (string) Hex encoded public key
      \"type\":\"type\", (string) The output type
      \"reqSigs\": n,    (numeric) The required signatures
      \"addresses\": [   (json array of string)
         \"address\"     (string) Dogecoin address
         ,...
      ],
      \"p2sh\",\"address\" (string) script address
    }"
  [hex]
  {:pre [(string? hex)]})


(defrpc dumpprivkey
  "dumpprivkey \"dogecoinaddress\"

  Reveals the private key corresponding to 'dogecoinaddress'.
  Then the importprivkey can be used with this output

  Arguments:

    1. \"dogecoinaddress\" (string, required) The Dogecoin address for
       the private key

  Result:
    \"key\" (string) The private key."
  [dogecoinaddress]
  {:pre [(string? dogecoinaddress)]})


(defrpc dumpwallet
  "dumpwallet \"filename\"

  Dumps all wallet keys in a human-readable format.

  Arguments:

    1. \"filename\" (string, required) The filename."
  [filename]
  {:pre [(string? filename)]})


(defrpc encryptwallet
  "encryptwallet \"passphrase\"

  Encrypts the wallet with 'passphrase'. This is for first time encryption.
  After this, any calls that interact with private keys such as sending or signing
  will require the passphrase to be set prior the making these calls.
  Use the walletpassphrase call for this, and then walletlock call.
  If the wallet is already encrypted, use the walletpassphrasechange call.
  Note that this will shutdown the server.

  Arguments:

    1. \"passphrase\" (string) The pass phrase to encrypt the wallet
       with. It must be at least 1 character, but should be long."
  [passphrase]
  {:pre [(string? passphrase)]})


(defrpc getaccount
  "getaccount \"dogecoinaddress\"

  Returns the account associated with the given address.

  Arguments:

    1. \"dogecoinaddress\" (string, required) The Dogecoin address for
       account lookup.

  Result:
    \"accountname\" (string) the account address."
  [dogecoinaddress]
  {:pre [(string? dogecoinaddress)]})


(defrpc getaccountaddress
  "getaccountaddress \"account\"

  Returns the current Dogecoin address for receiving payments to this account.

  Arguments:

    1. \"account\" (string, required) The account name for the
       address. It can also be set to the empty string \"\" to
       represent the default account. The account does not need to
       exist, it will be created and a new address created if there
       is no account by the given name.

  Result:
    \"dogecoinaddress\"   (string) The account Dogecoin address."
  [account]
  {:pre [(string? account)]})


(defrpc getaddednodeinfo
  "getaddednodeinfo dns ( \"node\" )

  Returns information about the given added node, or all added nodes
  (note that onetry addnodes are not listed here)
  If dns is false, only a list of added nodes will be provided,
  otherwise connected information will also be available.

  Arguments:

    1. dns (boolean, required) If false, only a list of added nodes
       will be provided, otherwise connected information will also be
       available.

    2. \"node\" (string, optional) If provided, return information
       about this specific node, otherwise all nodes are returned.

  Result:
    [
      {
        \"addednode\" : \"192.168.0.201\",   (string) The node ip address
        \"connected\" : true|false,          (boolean) If connected
        \"addresses\" : [
           {
             \"address\" : \"192.168.0.201:22556\",  (string) The Dogecoin server host and port
             \"connected\" : \"outbound\"            (string) connection, inbound or outbound
           }
           ,...
         ]
      }
      ,...
    ]"
  [dns node?]
  {:pre [(string? dns)]})


(defrpc getaddressesbyaccount
  "getaddednodeinfo dns ( \"node\" )

  Returns information about the given added node, or all added nodes
  (note that onetry addnodes are not listed here)
  If dns is false, only a list of added nodes will be provided,
  otherwise connected information will also be available.

  Arguments:

    1. dns (boolean, required) If false, only a list of added nodes
       will be provided, otherwise connected information will also be
       available.

    2. \"node\" (string, optional) If provided, return information
       about this specific node, otherwise all nodes are returned.

  Result:
    [
      {
        \"addednode\" : \"192.168.0.201\",   (string) The node ip address
        \"connected\" : true|false,          (boolean) If connected
        \"addresses\" : [
           {
             \"address\" : \"192.168.0.201:22556\",  (string) The Dogecoin server host and port
             \"connected\" : \"outbound\"           (string) connection, inbound or outbound
           }
           ,...
         ]
      }
      ,...
    ]"
  [account]
  {:pre [(string? account)]})


(defrpc getbalance
  "getbalance ( \"account\" minconf )

  If account is not specified, returns the server's total available balance.
  If account is specified, returns the balance in the account.
  Note that the account \"\" is not the same as leaving the parameter out.
  The server total may be different to the balance in the default \"\" account.

  Arguments:
    1. \"account\" (string, optional) The selected account, or \"*\"
       for entire wallet. It may be the default account using \"\".
    2. minconf (numeric, optional, default=1) Only include
       transactions confirmed at least this many times.

  Result:
    amount (numeric) The total amount in doge received for this account."
  [account? minconf?])


(defrpc getbestblockhash
  "getbestblockhash

  Returns the hash of the best (tip) block in the longest block chain.

  Result
    \"hex\" (string) the block hash hex encoded."
  [])


(defrpc getblock
  "getblock \"hash\" ( verbose )

  If verbose is false, returns a string that is serialized, hex-encoded data for block 'hash'.
  If verbose is true, returns an Object with information about block <hash>.

  Arguments:
    1. \"hash\" (string, required) The block hash

    2. verbose (boolean, optional, default=true) true for a json
       object, false for the hex encoded data

  Result (for verbose = true):
    {
      \"hash\" : \"hash\",              (string) the block hash (same as provided)
      \"confirmations\" : n,            (numeric) The number of confirmations
      \"size\" : n,                     (numeric) The block size
      \"height\" : n,                   (numeric) The block height or index
      \"version\" : n,                  (numeric) The block version
      \"merkleroot\" : \"xxxx\",        (string) The merkle root
      \"tx\" : [                        (array of string) The transaction ids
         \"transactionid\"              (string) The transaction id
         ,...
      ],
      \"time\" : ttt,                   (numeric) The block time in seconds since epoch (Jan 1 1970 GMT)
      \"nonce\" : n,                    (numeric) The nonce
      \"bits\" : \"1d00ffff\",          (string) The bits
      \"difficulty\" : x.xxx,           (numeric) The difficulty
      \"previousblockhash\" : \"hash\", (string) The hash of the previous block
      \"nextblockhash\" : \"hash\"      (string) The hash of the next block
    }

  Result (for verbose=false):
    \"data\" (string) A string that is serialized, hex-encoded data for block 'hash'."
  [hash verbose?]
  {:pre [(string? hash)]})


(defrpc getblockcount
  "getblockcount

  Returns the number of blocks in the longest block chain.

  Result:
    n (numeric) The current block count."
  [])


(defrpc getblockhash
  "getblockhash index

  Returns hash of block in best-block-chain at index provided.

  Arguments:
    1. index (numeric, required) The block index

  Result:
    \"hash\" (string) The block hash."
  [index]
  {:pre [(integer? index)]})


(defrpc getblocktemplate
  "getblocktemplate ( \"jsonrequestobject\" )

  If the request parameters include a 'mode' key, that is used to
  explicitly select between the default 'template' request or a
  'proposal'.  It returns data needed to construct a block to work on.
  See https://en.bitcoin.it/wiki/BIP_0022 for full specification.

  Arguments:
    1. \"jsonrequestobject\" (string, optional) A json object in the
       following spec
  
         {
           \"mode\":\"template\"   (string, optional) This must be set to \"template\" or omitted
           \"capabilities\":[      (array, optional) A list of strings
               \"support\"         (string) client side supported feature, 'longpoll', 'coinbasetxn', 'coinbasevalue', 'proposal', 'serverlist', 'workid'
               ,...
             ]
         }

  Result:
    {
      \"version\" : n,                    (numeric) The block version
      \"previousblockhash\" : \"xxxx\",   (string) The hash of current highest block
      \"transactions\" : [                (array) contents of non-coinbase transactions that should be included in the next block
          {
             \"data\" : \"xxxx\",         (string) transaction data encoded in hexadecimal (byte-for-byte)
             \"hash\" : \"xxxx\",         (string) hash/id encoded in little-endian hexadecimal
             \"depends\" : [              (array) array of numbers
                 n                        (numeric) transactions before this one (by 1-based index in 'transactions' list) that must be present in the final block if this one is
                 ,...
             ],
             \"fee\": n,                  (numeric) difference in value between transaction inputs and outputs (in Satoshis); for coinbase transactions, this is a negative Number of the total collected block fees (ie, not including the block subsidy); if key is not present, fee is unknown and clients MUST NOT assume there isn't one
             \"sigops\" : n,              (numeric) total number of SigOps, as counted for purposes of block limits; if key is not present, sigop count is unknown and clients MUST NOT assume there aren't any
             \"required\" : true|false    (boolean) if provided and true, this transaction must be in the final block
          }
          ,...
      ],
      \"coinbaseaux\" : {                 (json object) data that should be included in the coinbase's scriptSig content
          \"flags\" : \"flags\"           (string)
      },
      \"coinbasevalue\" : n,              (numeric) maximum allowable input to coinbase transaction, including the generation award and transaction fees (in Satoshis)
      \"coinbasetxn\" : { ... },          (json object) information for coinbase transaction
      \"target\" : \"xxxx\",              (string) The hash target
      \"mintime\" : xxx,                  (numeric) The minimum timestamp appropriate for next block time in seconds since epoch (Jan 1 1970 GMT)
      \"mutable\" : [                     (array of string) list of ways the block template may be changed
         \"value\"                        (string) A way the block template may be changed, e.g. 'time', 'transactions', 'prevblock'
         ,...
      ],
      \"noncerange\" : \"00000000ffffffff\",   (string) A range of valid nonces
      \"sigoplimit\" : n,                 (numeric) limit of sigops in blocks
      \"sizelimit\" : n,                  (numeric) limit of block size
      \"curtime\" : ttt,                  (numeric) current timestamp in seconds since epoch (Jan 1 1970 GMT)
      \"bits\" : \"xxx\",                 (string) compressed target of next block
      \"height\" : n                      (numeric) The height of the next block
    }"
  [params?])


(defrpc getconnectioncount
  "getblocktemplate ( \"jsonrequestobject\" )

  If the request parameters include a 'mode' key, that is used to
  explicitly select between the default 'template' request or a
  'proposal'.  It returns data needed to construct a block to work on.
  See https://en.bitcoin.it/wiki/BIP_0022 for full specification.

  Arguments:

    1. \"jsonrequestobject\" (string, optional) A json object in the
       following spec
       {
         \"mode\":\"template\"    (string, optional) This must be set to \"template\" or omitted
         \"capabilities\":[       (array, optional) A list of strings
             \"support\"           (string) client side supported feature, 'longpoll', 'coinbasetxn', 'coinbasevalue', 'proposal', 'serverlist', 'workid'
             ,...
           ]
       }

  Result:
    {
      \"version\" : n,                    (numeric) The block version
      \"previousblockhash\" : \"xxxx\",    (string) The hash of current highest block
      \"transactions\" : [                (array) contents of non-coinbase transactions that should be included in the next block
          {
             \"data\" : \"xxxx\",          (string) transaction data encoded in hexadecimal (byte-for-byte)
             \"hash\" : \"xxxx\",          (string) hash/id encoded in little-endian hexadecimal
             \"depends\" : [              (array) array of numbers
                 n                        (numeric) transactions before this one (by 1-based index in 'transactions' list) that must be present in the final block if this one is
                 ,...
             ],
             \"fee\": n,                   (numeric) difference in value between transaction inputs and outputs (in Satoshis); for coinbase transactions, this is a negative Number of the total collected block fees (ie, not including the block subsidy); if key is not present, fee is unknown and clients MUST NOT assume there isn't one
             \"sigops\" : n,               (numeric) total number of SigOps, as counted for purposes of block limits; if key is not present, sigop count is unknown and clients MUST NOT assume there aren't any
             \"required\" : true|false     (boolean) if provided and true, this transaction must be in the final block
          }
          ,...
      ],
      \"coinbaseaux\" : {                  (json object) data that should be included in the coinbase's scriptSig content
          \"flags\" : \"flags\"            (string)
      },
      \"coinbasevalue\" : n,               (numeric) maximum allowable input to coinbase transaction, including the generation award and transaction fees (in Satoshis)
      \"coinbasetxn\" : { ... },           (json object) information for coinbase transaction
      \"target\" : \"xxxx\",               (string) The hash target
      \"mintime\" : xxx,                   (numeric) The minimum timestamp appropriate for next block time in seconds since epoch (Jan 1 1970 GMT)
      \"mutable\" : [                      (array of string) list of ways the block template may be changed
         \"value\"                         (string) A way the block template may be changed, e.g. 'time', 'transactions', 'prevblock'
         ,...
      ],
      \"noncerange\" : \"00000000ffffffff\",   (string) A range of valid nonces
      \"sigoplimit\" : n,                 (numeric) limit of sigops in blocks
      \"sizelimit\" : n,                  (numeric) limit of block size
      \"curtime\" : ttt,                  (numeric) current timestamp in seconds since epoch (Jan 1 1970 GMT)
      \"bits\" : \"xxx\",                 (string) compressed target of next block
      \"height\" : n                      (numeric) The height of the next block
  }"
  [])


(defrpc getdifficulty
  "getdifficulty

  Returns the proof-of-work difficulty as a multiple of the minimum
  difficulty.

  Result:
    n.nnn (numeric) the proof-of-work difficulty as a multiple of the minimum difficulty."
  [])


(defrpc getgenerate
  "getgenerate

  Return if the server is set to generate coins or not. The default is
  false. It is set with the command line argument -gen (or
  dogecoin.conf setting gen) It can also be set with the setgenerate
  call.

  Result
    true|false (boolean) If the server is set to generate coins or not."
  [])


(defrpc gethashespersec
  "gethashespersec

  Returns a recent hashes per second performance measurement while
  generating. See the getgenerate and setgenerate calls to turn
  generation on and off.

  Result:
    n (numeric) The recent hashes per second when generation is
    on (will return 0 if generation is off)."
  [])


(defrpc getinfo
  "getinfo

  Returns an object containing various state info.

  Result:
    {
      \"version\": xxxxx,           (numeric) the server version
      \"protocolversion\": xxxxx,   (numeric) the protocol version
      \"walletversion\": xxxxx,     (numeric) the wallet version
      \"balance\": xxxxxxx,         (numeric) the total Dogecoin balance of the wallet
      \"blocks\": xxxxxx,           (numeric) the current number of blocks processed in the server
      \"timeoffset\": xxxxx,        (numeric) the time offset
      \"connections\": xxxxx,       (numeric) the number of connections
      \"proxy\": \"host:port\",     (string, optional) the proxy used by the server
      \"difficulty\": xxxxxx,       (numeric) the current difficulty
      \"testnet\": true|false,      (boolean) if the server is using testnet or not
      \"keypoololdest\": xxxxxx,    (numeric) the timestamp (seconds since GMT epoch) of the oldest pre-generated key in the key pool
      \"keypoolsize\": xxxx,        (numeric) how many new keys are pre-generated
      \"paytxfee\": x.xxxx,         (numeric) the transaction fee set in doge
      \"unlocked_until\": ttt,      (numeric) the timestamp in seconds since epoch (midnight Jan 1 1970 GMT) that the wallet is unlocked for transfers  , or 0 if the wallet is locked
    \"errors\": \"...\"             (string) any error messages
    }"
  [])


(defrpc getmininginfo
  "getmininginfo

  Returns a json object containing mining-related information.
  Result:
  {
    \"blocks\": nnn,             (numeric) The current block
    \"currentblocksize\": nnn,   (numeric) The last block size
    \"currentblocktx\": nnn,     (numeric) The last block transaction
    \"difficulty\": xxx.xxxxx    (numeric) The current difficulty
    \"errors\": \"...\"          (string) Current errors
    \"generate\": true|false     (boolean) If the generation is on or off (see getgenerate or setgenerate calls)
    \"genproclimit\": n          (numeric) The processor limit for generation. -1 if no generation. (see getgenerate or setgenerate calls)
    \"hashespersec\": n          (numeric) The hashes per second of the generation, or 0 if no generation.
    \"pooledtx\": n              (numeric) The size of the mem pool
    \"testnet\": true|false      (boolean) If using testnet or not
  }"
  [])


(defrpc getnettotals
  "getnettotals

  Returns information about network traffic, including bytes in, bytes
  out, and current time.

  Result:
    {
      \"totalbytesrecv\": n,   (numeric) Total bytes received
      \"totalbytessent\": n,   (numeric) Total bytes sent
      \"timemillis\": t        (numeric) Total cpu time
    }"
  [])


(defrpc getnetworkhashps
  "getnetworkhashps ( blocks height )

  Returns the estimated network hashes per second based on the last n
  blocks. Pass in [blocks] to override # of blocks, -1 specifies
  since last difficulty change. Pass in [height] to estimate the
  network speed at the time when a certain block was found.

  Arguments:

    1. blocks (numeric, optional, default=120) The number of blocks,
       or -1 for blocks since last difficulty change.

    2. height (numeric, optional, default=-1) To estimate at the time
       of the given height.

  Result:
    x (numeric) Hashes per second estimated."
  [blocks? height])


(defrpc getnewaddress
  "getnewaddress ( \"account\" )

  Returns a new Dogecoin address for receiving payments.
  If 'account' is specified (recommended), it is added to the address book
  so payments received with the address will be credited to 'account'.

  Arguments:
    1. \"account\" (string, optional) The account name for the address
       to be linked to. if not provided, the default account \"\" is
       used. It can also be set to the empty string \"\" to represent
       the default account. The account does not need to exist, it
       will be created if there is no account by the given name.

  Result:
    \"dogecoinaddress\" (string) The new Dogecoin address."
  [account?])


(defrpc getpeerinfo
  "getpeerinfo

  Returns data about each connected network node as a json array of objects.

  Result:
    [
      {
        \"addr\":\"host:port\",      (string) The ip address and port of the peer
        \"addrlocal\":\"ip:port\",   (string) local address
        \"services\":\"00000001\",   (string) The services
        \"lastsend\": ttt,           (numeric) The time in seconds since epoch (Jan 1 1970 GMT) of the last send
        \"lastrecv\": ttt,           (numeric) The time in seconds since epoch (Jan 1 1970 GMT) of the last receive
        \"bytessent\": n,            (numeric) The total bytes sent
        \"bytesrecv\": n,            (numeric) The total bytes received
        \"conntime\": ttt,           (numeric) The connection time in seconds since epoch (Jan 1 1970 GMT)
        \"pingtime\": n,             (numeric) ping time
        \"pingwait\": n,             (numeric) ping wait
        \"version\": v,              (numeric) The peer version, such as 7001
        \"subver\": \"/Satoshi:0.8.5/\",  (string) The string version
        \"inbound\": true|false,     (boolean) Inbound (true) or Outbound (false)
        \"startingheight\": n,       (numeric) The starting height (block) of the peer
        \"banscore\": n,              (numeric) The ban score (stats.nMisbehavior)
        \"syncnode\" : true|false     (booleamn) if sync node
      }
      ,...
    }"
  [])


(defrpc getrawchangeaddress
  "getrawchangeaddress

  Returns a new Dogecoin address, for receiving change.
  This is for use with raw transactions, NOT normal use.

  Result:
    \"address\"    (string) The address."
  [])


(defrpc getrawmempool
  "getrawmempool ( verbose )

  Returns all transaction ids in memory pool as a json array of string
  transaction ids.

  Arguments:
    1. verbose (boolean, optional, default=false) true for a json
       object, false for array of transaction ids

  Result: (for verbose = false):
    [                     (json array of string)
      \"transactionid\"     (string) The transaction id
      ,...
    ]

  Result: (for verbose = true):
    {                           (json object)
      \"transactionid\" : {       (json object)
        \"size\" : n,             (numeric) transaction size in bytes
        \"fee\" : n,              (numeric) transaction fee in Dogecoins
        \"time\" : n,             (numeric) local time transaction entered pool in seconds since 1 Jan 1970 GMT
        \"height\" : n,           (numeric) block height when transaction entered pool
        \"startingpriority\" : n, (numeric) priority when transaction entered pool
        \"currentpriority\" : n,  (numeric) transaction priority now
        \"depends\" : [           (array) unconfirmed transactions used as inputs for this transaction
            \"transactionid\",    (string) parent transaction id
           ... ]
      }, ...
    ]"
  [verbose?])


(defrpc getrawtransaction
  "getrawtransaction \"txid\" ( verbose )

  Return the raw transaction data.

  If verbose=0, returns a string that is serialized, hex-encoded data
  for 'txid'.  If verbose is non-zero, returns an Object with
  information about 'txid'.

  Arguments:

    1. \"txid\" (string, required) The transaction id

    2. verbose (numeric, optional, default=0) If 0, return a string,
       other return a json object

  Result (if verbose is not set or set to 0):
    \"data\"      (string) The serialized, hex-encoded data for 'txid'

  Result (if verbose > 0):
    {
      \"hex\" : \"data\",       (string) The serialized, hex-encoded data for 'txid'
      \"txid\" : \"id\",        (string) The transaction id (same as provided)
      \"version\" : n,          (numeric) The version
      \"locktime\" : ttt,       (numeric) The lock time
      \"vin\" : [               (array of json objects)
         {
           \"txid\": \"id\",    (string) The transaction id
           \"vout\": n,         (numeric)
           \"scriptSig\": {     (json object) The script
             \"asm\": \"asm\",  (string) asm
             \"hex\": \"hex\"   (string) hex
           },
           \"sequence\": n      (numeric) The script sequence number
         }
         ,...
      ],
      \"vout\" : [              (array of json objects)
         {
           \"value\" : x.xxx,            (numeric) The value in doge
           \"n\" : n,                    (numeric) index
           \"scriptPubKey\" : {          (json object)
             \"asm\" : \"asm\",          (string) the asm
             \"hex\" : \"hex\",          (string) the hex
             \"reqSigs\" : n,            (numeric) The required sigs
             \"type\" : \"pubkeyhash\",  (string) The type, eg 'pubkeyhash'
             \"addresses\" : [           (json array of string)
               \"dogecoinaddress\"        (string) Dogecoin address
               ,...
             ]
           }
         }
         ,...
      ],
      \"blockhash\" : \"hash\",   (string) the block hash
      \"confirmations\" : n,      (numeric) The confirmations
      \"time\" : ttt,             (numeric) The transaction time in seconds since epoch (Jan 1 1970 GMT)
      \"blocktime\" : ttt         (numeric) The block time in seconds since epoch (Jan 1 1970 GMT)
  }"
  [txid verbose?]
  {:pre [(string? txid)]})


(defrpc getreceivedbyaccount
  "getreceivedbyaccount \"account\" ( minconf )

  Returns the total amount received by addresses with <account> in transactions with at least [minconf] confirmations.

  Arguments:

    1. \"account\" (string, required) The selected account, may be the
       default account using \"\".

    2. minconf (numeric, optional, default=1) Only include
       transactions confirmed at least this many times.

  Result:
    amount (numeric) The total amount in doge received for this account."
  [account minconf?])


(defrpc getreceivedbyaddress
  "getreceivedbyaddress \"dogecoinaddress\" ( minconf )

  Returns the total amount received by the given dogecoinaddress in
  transactions with at least minconf confirmations.

  Arguments:

    1. \"dogecoinaddress\" (string, required) The Dogecoin address for
       transactions.

    2. minconf (numeric, optional, default=1) Only include
       transactions confirmed at least this many times.

  Result:
    amount   (numeric) The total amount in doge received at this address."
  [dogecoinaddress minconf?]
  {:pre [(string? dogecoinaddress)]})


(defrpc gettransaction
  "gettransaction \"txid\"

  Get detailed information about in-wallet transaction <txid>

  Arguments:

    1. \"txid\" (string, required) The transaction id

  Result:
    {
      \"amount\" : x.xxx,        (numeric) The transaction amount in doge
      \"confirmations\" : n,     (numeric) The number of confirmations
      \"blockhash\" : \"hash\",  (string) The block hash
      \"blockindex\" : xx,       (numeric) The block index
      \"blocktime\" : ttt,       (numeric) The time in seconds since epoch (1 Jan 1970 GMT)
      \"txid\" : \"transactionid\",   (string) The transaction id, see also https://blockchain.info/tx/[transactionid]
      \"time\" : ttt,            (numeric) The transaction time in seconds since epoch (1 Jan 1970 GMT)
      \"timereceived\" : ttt,    (numeric) The time received in seconds since epoch (1 Jan 1970 GMT)
      \"details\" : [
        {
          \"account\" : \"accountname\",  (string) The account name involved in the transaction, can be \"\" for the default account.
          \"address\" : \"dogecoinaddress\",   (string) The Dogecoin address involved in the transaction
          \"category\" : \"send|receive\",    (string) The category, either 'send' or 'receive'
          \"amount\" : x.xxx                  (numeric) The amount in doge
        }
        ,...
      ],
      \"hex\" : \"data\"         (string) Raw data for transaction
    }"
  [txid]
  {:pre [(string? txid)]})


(defrpc gettxout
  "gettxout \"txid\" n ( includemempool )

  Returns details about an unspent transaction output.

  Arguments:

    1. \"txid\" (string, required) The transaction id

    2. n (numeric, required) vout value

    3. includemempool (boolean, optional) Whether to included the mem
       pool

  Result:
    {
      \"bestblock\" : \"hash\",    (string) the block hash
      \"confirmations\" : n,       (numeric) The number of confirmations
      \"value\" : x.xxx,           (numeric) The transaction value in doge
      \"scriptPubKey\" : {         (json object)
         \"asm\" : \"code\",       (string)
         \"hex\" : \"hex\",        (string)
         \"reqSigs\" : n,          (numeric) Number of required signatures
         \"type\" : \"pubkeyhash\", (string) The type, eg pubkeyhash
         \"addresses\" : [          (array of string) array of dogecoin addresses
            \"bitcoinaddress\"     (string) bitcoin address
            ,...
         ]
      },
      \"version\" : n,            (numeric) The version
      \"coinbase\" : true|false   (boolean) Coinbase or not
    }"
  [txid n includemempool?]
  {:pre [(string? txid)
         (integer? n)]})


(defrpc gettxoutsetinfo
  "gettxoutsetinfo

  Returns statistics about the unspent transaction output set.  Note
  this call may take some time.

  Result:
    {
      \"height\":n,     (numeric) The current block height (index)
      \"bestblock\": \"hex\",   (string) the best block hash hex
      \"transactions\": n,      (numeric) The number of transactions
      \"txouts\": n,            (numeric) The number of output transactions
      \"bytes_serialized\": n,  (numeric) The serialized size
      \"hash_serialized\": \"hash\",   (string) The serialized hash
      \"total_amount\": x.xxx          (numeric) The total amount
    }"
  [])


(defrpc getunconfirmedbalance
  "getunconfirmedbalance

  Returns the server's total unconfirmed balance."
  [])


(defrpc getwork
  "getunconfirmedbalance

  Returns the server's total unconfirmed balance."
  [])


(defrpc help
  "List commands, or get help for a command."
  [command?])


(defrpc importprivkey
  "importprivkey \"dogecoinprivkey\" ( \"label\" rescan )

  Adds a private key (as returned by dumpprivkey) to your wallet.

  Arguments:

    1. \"dogecoinprivkey\" (string, required) The private key (see
       dumpprivkey)

    2. \"label\" (string, optional) an optional label

    3. rescan (boolean, optional, default=true) Rescan the wallet for
       transactions."
  [dogecoinprivkey label? rescan?]
  {:pre [(string? dogecoinprivkey)
         (or (= nil label?)
             (string? label?))]})


(defrpc importwallet
  "importwallet \"filename\"

  Imports keys from a wallet dump file (see dumpwallet).

  Arguments:

    1. \"filename\"    (string, required) The wallet file."
  [filename]
  {:pre [(string? filename)]})


(defrpc keypoolrefill
  "keypoolrefill ( newsize )

  Fills the keypool.

  Arguments

    1. newsize (numeric, optional, default=100) The new keypool size."
  [newsize?])


(defrpc listaccounts
  "listaccounts ( minconf )

  Returns Object that has account names as keys, account balances as values.

  Arguments:
    1. minconf (numeric, optional, default=1) Only onclude transactions with at least this many confirmations

  Result:
    {                      (json object where keys are account names, and values are numeric balances
      \"account\": x.xxx,  (numeric) The property name is the account name, and the value is the total balance for the account.
      ...
    }"
  [minconf?])


(defrpc listaddressgroupings
  "listaddressgroupings

  Lists groups of addresses which have had their common ownership
  made public by common use as inputs or as the resulting change
  in past transactions

  Result:
    [
      [
        [
          \"dogecoinaddress\",     (string) The Dogecoin address
          amount,                 (numeric) The amount in doge
          \"account\"             (string, optional) The account
        ]
        ,...
      ]
      ,...
    ]"
  [])


(defrpc listlockunspent
  "listlockunspent

  Returns list of temporarily unspendable outputs.
  See the lockunspent call to lock and unlock transactions for spending.

  Result:
    [
      {
        \"txid\" : \"transactionid\",     (string) The transaction id locked
        \"vout\" : n                      (numeric) The vout value
      }
      ,...
    ]"
  [])


(defrpc listreceivedbyaccount
  "listreceivedbyaccount ( minconf includeempty )

  List balances by account.

  Arguments:

    1. minconf (numeric, optional, default=1) The minimum number of
       confirmations before payments are included.

    2. includeempty (boolean, optional, default=false) Whether to
       include accounts that haven't received any payments.

  Result:
    [
      {
        \"account\" : \"accountname\",  (string) The account name of the receiving account
        \"amount\" : x.xxx,             (numeric) The total amount received by addresses with this account
        \"confirmations\" : n           (numeric) The number of confirmations of the most recent transaction included
      }
      ,...
    ]"
  [minconf? includeempty?])


(defrpc listreceivedbyaddress
  "listreceivedbyaddress ( minconf includeempty )

  List balances by receiving address.

  Arguments:

    1. minconf (numeric, optional, default=1) The minimum number of
       confirmations before payments are included.

    2. includeempty (numeric, optional, dafault=false) Whether to
       include addresses that haven't received any payments.

  Result:
    [
      {
        \"address\" : \"receivingaddress\",  (string) The receiving address
        \"account\" : \"accountname\",       (string) The account of the receiving address. The default account is \"\".
        \"amount\" : x.xxx,                  (numeric) The total amount in doge received by the address
        \"confirmations\" : n                (numeric) The number of confirmations of the most recent transaction included
      }
      ,...
    ]"
  [minconf? includeempty?])


(defrpc listsinceblock
  "listsinceblock ( \"blockhash\" target-confirmations )

  Get all transactions in blocks since block [blockhash], or all transactions if omitted

  Arguments:
    1. \"blockhash\"   (string, optional) The block hash to list transactions since
    2. target-confirmations:    (numeric, optional) The confirmations required, must be 1 or more

  Result:
    {
      \"transactions\": [
        \"account\":\"accountname\",       (string) The account name associated with the transaction. Will be \"\" for the default account.
        \"address\":\"dogecoinaddress\",    (string) The Dogecoin address of the transaction. Not present for move transactions (category = move).
        \"category\":\"send|receive\",     (string) The transaction category. 'send' has negative amounts, 'receive' has positive amounts.
        \"amount\": x.xxx,          (numeric) The amount in doge. This is negative for the 'send' category, and for the 'move' category for moves
                                              outbound. It is positive for the 'receive' category, and for the 'move' category for inbound funds.
        \"fee\": x.xxx,             (numeric) The amount of the fee in doge. This is negative and only available for the 'send' category of transactions.
        \"confirmations\": n,       (numeric) The number of confirmations for the transaction. Available for 'send' and 'receive' category of transactions.
        \"blockhash\": \"hashvalue\",     (string) The block hash containing the transaction. Available for 'send' and 'receive' category of transactions.
        \"blockindex\": n,          (numeric) The block index containing the transaction. Available for 'send' and 'receive' category of transactions.
        \"blocktime\": xxx,         (numeric) The block time in seconds since epoch (1 Jan 1970 GMT).
        \"txid\": \"transactionid\",  (string) The transaction id (see https://blockchain.info/tx/[transactionid]. Available for 'send' and 'receive' category of transactions.
        \"time\": xxx,              (numeric) The transaction time in seconds since epoch (Jan 1 1970 GMT).
        \"timereceived\": xxx,      (numeric) The time received in seconds since epoch (Jan 1 1970 GMT). Available for 'send' and 'receive' category of transactions.
        \"comment\": \"...\",       (string) If a comment is associated with the transaction.
        \"to\": \"...\",            (string) If a comment to is associated with the transaction.
      ],
      \"lastblock\": \"lastblockhash\"     (string) The hash of the last block
    }"
  [blockhash? target-confirmations?])


(defrpc listtransactions
  "listtransactions ( \"account\" count from )

  Returns up to 'count' most recent transactions skipping the first 'from' transactions for account 'account'.

  Arguments:

    1. \"account\" (string, optional) The account name. If not
       included, it will list all transactions for all accounts.  If
       \"\" is set, it will list transactions for the default account.

    2. count (numeric, optional, default=10) The number of
       transactions to return

    3. from (numeric, optional, default=0) The number of transactions
        to skip

  Result:
    [
      {
        \"account\":\"accountname\",       (string) The account name associated with the transaction.
                                                    It will be \"\" for the default account.
        \"address\":\"dogecoinaddress\",    (string) The Dogecoin address of the transaction. Not present for
                                                    move transactions (category = move).
        \"category\":\"send|receive|move\", (string) The transaction category. 'move' is a local (off blockchain)
                                                    transaction between accounts, and not associated with an address,
                                                    transaction id or block. 'send' and 'receive' transactions are
                                                    associated with an address, transaction id and block details
        \"amount\": x.xxx,          (numeric) The amount in doge. This is negative for the 'send' category, and for the
                                             'move' category for moves outbound. It is positive for the 'receive' category,
                                             and for the 'move' category for inbound funds.
        \"fee\": x.xxx,             (numeric) The amount of the fee in doge. This is negative and only available for the
                                             'send' category of transactions.
        \"confirmations\": n,       (numeric) The number of confirmations for the transaction. Available for 'send' and
                                             'receive' category of transactions.
        \"blockhash\": \"hashvalue\", (string) The block hash containing the transaction. Available for 'send' and 'receive'
                                              category of transactions.
        \"blockindex\": n,          (numeric) The block index containing the transaction. Available for 'send' and 'receive'
                                              category of transactions.
        \"txid\": \"transactionid\", (string) The transaction id (see https://blockchain.info/tx/[transactionid]. Available
                                              for 'send' and 'receive' category of transactions.
        \"time\": xxx,              (numeric) The transaction time in seconds since epoch (midnight Jan 1 1970 GMT).
        \"timereceived\": xxx,      (numeric) The time received in seconds since epoch (midnight Jan 1 1970 GMT). Available
                                              for 'send' and 'receive' category of transactions.
        \"comment\": \"...\",       (string) If a comment is associated with the transaction.
        \"otheraccount\": \"accountname\",  (string) For the 'move' category of transactions, the account the funds came
                                              from (for receiving funds, positive amounts), or went to (for sending funds,
                                              negative amounts).
      }
    ]"
  [account? count? from?])


(defrpc listunspent
  "listunspent ( minconf maxconf  [\"address\",...] )

  Returns array of unspent transaction outputs
  with between minconf and maxconf (inclusive) confirmations.
  Optionally filter to only include txouts paid to specified addresses.
  Results are an array of Objects, each of which has:
  {txid, vout, scriptPubKey, amount, confirmations}

  Arguments:

    1. minconf (numeric, optional, default=1) The minimum
       confirmationsi to filter

    2. maxconf (numeric, optional, default=9999999) The maximum
       confirmations to filter

    3. \"addresses\" (string) A json array of Dogecoin addresses to
       filter

        [
          \"address\"   (string) Dogecoin address
          ,...
        ]

  Result
   [                   (array of json object)
     {
       \"txid\" : \"txid\",        (string) the transaction id
       \"vout\" : n,               (numeric) the vout value
       \"address\" : \"address\",  (string) the Dogecoin address
       \"account\" : \"account\",  (string) The associated account, or \"\" for the default account
       \"scriptPubKey\" : \"key\", (string) the script key
       \"amount\" : x.xxx,         (numeric) the transaction amount in doge
       \"confirmations\" : n       (numeric) The number of confirmations
     }
     ,...
   ]"
  [minconf? maxconf?])


(defrpc lockunspent
  "lockunspent unlock [{\"txid\":\"txid\",\"vout\":n},...]

  Updates list of temporarily unspendable outputs.
  Temporarily lock (lock=true) or unlock (lock=false) specified transaction outputs.
  A locked transaction output will not be chosen by automatic coin selection, when spending dogecoins.
  Locks are stored in memory only. Nodes start with zero locked outputs, and the locked output list
  is always cleared (by virtue of process exit) when a node stops or fails.
  Also see the listunspent call

  Arguments:

    1. unlock (boolean, required) Whether to unlock (true) or
       lock (false) the specified transactions

    2. \"transactions\" (string, required) A json array of
       objects. Each object the txid (string) vout (numeric)

         [                       (json array of json objects)
           {
             \"txid\":\"id\",    (string) The transaction id
             \"vout\": n         (numeric) The output number
           }
           ,...
         ]

  Result:
    true|false (boolean) Whether the command was successful or not."
  [unlock array-of-objects]
  {:pre [(not-nil? unlock)]})


(defrpc move
  "move \"fromaccount\" \"toaccount\" amount ( minconf \"comment\" )

  Move a specified amount from one account in your wallet to another.

  Arguments:

    1. \"fromaccount\" (string, required) The name of the account to
       move funds from. May be the default account using \"\".

    2. \"toaccount\" (string, required) The name of the account to
       move funds to. May be the default account using \"\".

    3. minconf (numeric, optional, default=1) Only use funds with at
       least this many confirmations.

    4. \"comment\" (string, optional) An optional comment, stored in
       the wallet only.

  Result:
    true|false           (boolean) true if successfull."
  [fromaccount toaccount amount minconf? comment?]
  {:pre [(string? fromaccount)
         (string? toaccount)
         (number? amount)]})


(defrpc ping
  "ping

  Requests that a ping be sent to all other nodes, to measure ping
  time.  Results provided in getpeerinfo, pingtime and pingwait fields
  are decimal seconds.  Ping command is handled in queue with all
  other commands, so it measures processing backlog, not just network
  ping."
  [])


(defrpc sendfrom
  "sendfrom \"fromaccount\" \"todogecoinaddress\" amount ( minconf \"comment\" \"comment-to\" )

  Sent an amount from an account to a Dogecoin address.
  The amount is a real and is rounded to the nearest 0.00000001.

  Arguments:

    1. \"fromaccount\" (string, required) The name of the account to
       send funds from. May be the default account using \"\".

    2. \"todogecoinaddress\" (string, required) The Dogecoin address
        to send funds to.

    3. amount (numeric, required) The amount in doge. (transaction fee
       is added on top).

    4. minconf (numeric, optional, default=1) Only use funds with at
       least this many confirmations.

    5. \"comment\" (string, optional) A comment used to store what the
       transaction is for.  This is not part of the transaction, just
       kept in your wallet.

    6. \"comment-to\" (string, optional) An optional comment to store
       the name of the person or organization to which you're sending
       the transaction. This is not part of the transaction, it is
       just kept in your wallet.

  Result:
    \"transactionid\" (string) The transaction id."
  [fromaccount todogecoinaddress amount minconf comment comment-to]
  {:pre [(string? fromaccount)
         (string? todogecoinaddress)
         (number? amount)]})


(defrpc sendmany
  "sendmany \"fromaccount\" {\"address\":amount,...} ( minconf \"comment\" )

  Send multiple times. Amounts are double-precision floating point
  numbers.

  Arguments:

    1. \"fromaccount\" (string, required) The account to send the
       funds from, can be \"\" for the default account

    2. \"amounts\" (string, required) A json object with addresses and amounts
        {
          \"address\":amount   (numeric) The Dogecoin address is the key, the numeric amount in doge is the value
          ,...
        }

    3. minconf (numeric, optional, default=1) Only use the balance
       confirmed at least this many times.

    4. \"comment\" (string, optional) A comment

  Result:
    \"transactionid\" (string) The transaction id for the send. Only 1
    transaction is created regardless of the number of addresses."
    [fromaccount address-amount-maps minconf? comment?]  {:pre
    [(string? fromaccount)
         (vector? address-amount-maps)]})


(defrpc sendrawtransaction
  "sendrawtransaction \"hexstring\" ( allowhighfees )

  Submits raw transaction (serialized, hex-encoded) to local node and network.

  Also see createrawtransaction and signrawtransaction calls.

  Arguments:

    1. \"hexstring\" (string, required) The hex string of the raw
       transaction)

    2. allowhighfees (boolean, optional, default=false) Allow high
       fees

  Result:
    \"hex\"             (string) The transaction hash in hex."
  [hexstring allowhighfees?]
  {:pre [(string? hexstring)]})


(defrpc sendtoaddress
  "sendtoaddress \"dogecoinaddress\" amount ( \"comment\" \"comment-to\" )

  Sent an amount to a given address. The amount is a real and is
  rounded to the nearest 0.00000001

  Arguments:

  1. \"dogecoinaddress\" (string, required) The Dogecoin address to
      send to.

  2. \"amount\" (numeric, required) The amount in doge to send.

  3. \"comment\" (string, optional) A comment used to store what the
     transaction is for.  This is not part of the transaction, just
     kept in your wallet.

  4. \"comment-to\" (string, optional) A comment to store the name of
     the person or organization to which you're sending the
     transaction. This is not part of the transaction, just kept in
     your wallet.

  Result:
    \"transactionid\" (string) The transaction id."
  [dogecoinaddress amount comment? comment-to?]
  {:pre [(string? dogecoinaddress)
         (number? amount)]})


(defrpc setaccount
  "setaccount \"dogecoinaddress\" \"account\"

  Sets the account associated with the given address.

  Arguments:

    1. \"dogecoinaddress\" (string, required) The Dogecoin address to
       be associated with an account.
    2. \"account\" (string, required) The account to assign the
       address to."
  [dogecoinaddress account]
  {:pre [(string? dogecoinaddress)
         (string? account)]})


(defrpc setgenerate
  "setgenerate generate ( genproclimit )

  Set 'generate' true or false to turn generation on or off.
  Generation is limited to 'genproclimit' processors, -1 is unlimited.
  See the getgenerate call for the current setting.

  Arguments:

    1. generate (boolean, required) Set to true to turn on generation,
       off to turn off.

    2. genproclimit (numeric, optional) Set the processor limit for
       when generation is on. Can be -1 for unlimited.  Note: in
       -regtest mode, genproclimit controls how many blocks are
       generated immediately."

  [generate genproclimit?]
  {:pre [(instance? Boolean generate)]})


(defrpc settxfee
  "settxfee amount

  Set the transaction fee per kB.

  Arguments:

    1. amount (numeric, required) The transaction fee in DOGE/kB rounded to the nearest 0.00000001

  Result:
    true|false (boolean) Returns true if successful."
  [amount]
  {:pre [(number? amount)]})


(defrpc signmessage
  "signmessage \"dogecoinaddress\" \"message\"

  Sign a message with the private key of an address

  Arguments:

    1. \"dogecoinaddress\" (string, required) The Dogecoin address to
       use for the private key.

    2. \"message\" (string, required) The message to create a
       signature of.

  Result:
    \"signature\"          (string) The signature of the message encoded in base 64."
  [dogecoinaddress message]
  {:pre [(string? dogecoinaddress)
         (string? message)]})


(defrpc signrawtransaction
  "signrawtransaction \"hexstring\" ( [{\"txid\":\"id\",\"vout\":n,\"scriptPubKey\":\"hex\",\"redeemScript\":\"hex\"},...] [\"privatekey1\",...] sighashtype )

   Sign inputs for raw transaction (serialized, hex-encoded).  The
   second optional argument (may be null) is an array of previous
   transaction outputs that this transaction depends on but may not
   yet be in the block chain.  The third optional argument (may be
   null) is an array of base58-encoded private keys that, if given,
   will be the only keys used to sign the transaction.

  Arguments:

    1. \"hexstring\" (string, required) The transaction hex string

    2. \"prevtxs\" (string, optional) An json array of previous
       dependent transaction outputs

         [               (json array of json objects, or 'null' if none provided)
           {
             \"txid\":\"id\",           (string, required) The transaction id
             \"vout\":n,                (numeric, required) The output number
             \"scriptPubKey\": \"hex\", (string, required) script key
             \"redeemScript\": \"hex\"  (string, required) redeem script
           }
           ,...
        ]

    3. \"privatekeys\" (string, optional) A json array of
       base58-encoded private keys for signing

        [                  (json array of strings, or 'null' if none provided)
          \"privatekey\"   (string) private key in base58-encoding
          ,...
        ]

    4. \"sighashtype\" (string, optional, default=ALL) The signature
       has type. Must be one of

           \"ALL\"
           \"NONE\"
           \"SINGLE\"
           \"ALL|ANYONECANPAY\"
           \"NONE|ANYONECANPAY\"
           \"SINGLE|ANYONECANPAY\"

  Result:
    {
      \"hex\": \"value\",   (string) The raw transaction with signature(s) (hex-encoded string)
      \"complete\": n       (numeric) if transaction has a complete set of signature (0 if not)
    }"
  [hexstring txinfo privatekeys]
  {:pre [(string? hexstring)
         (vector? txinfo)]})


(defrpc stop
  "stop

  Stop Dogecoin server."
  [])


(defrpc submitblock
  "submitblock \"hexdata\" ( \"jsonparametersobject\" )

  Attempts to submit new block to network.
  The 'jsonparametersobject' parameter is currently ignored.
  See https://en.bitcoin.it/wiki/BIP_0022 for full specification.

  Arguments

  1. \"hexdata\" (string, required) the hex-encoded block data to
     submit

  2. \"jsonparametersobject\" (string, optional) object of optional
     parameters

      {
        \"workid\" : \"id\"   (string, optional) if the server provided a workid, it MUST be included with submissions
      }"
  [hex data optional-params-obj])


(defrpc validateaddress
  "validateaddress \"dogecoinaddress\"

  Return information about the given dogecoin address.

  Arguments:
    1. \"dogecoinaddress\"     (string, required) The dogecoin address to validate

  Result:
    {
      \"isvalid\" : true|false,          (boolean) If the address is valid or not. If not, this is the only property returned.
      \"address\" : \"dogecoinaddress\", (string) The dogecoin address validated
      \"ismine\" : true|false,           (boolean) If the address is yours or not
      \"isscript\" : true|false,         (boolean) If the key is a script
      \"pubkey\" : \"publickeyhex\",     (string) The hex value of the raw public key
      \"iscompressed\" : true|false,     (boolean) If the address is compressed
      \"account\" : \"account\"          (string) The account associated with the address, \"\" is the default account
    }"
  [dogecoinaddress]
  {:pre [(string? dogecoinaddress)]})


(defrpc verifychain
  "verifychain ( checklevel numblocks )

  Verifies blockchain database.

  Arguments:

    1. checklevel (numeric, optional, 0-4, default=3) How thorough the
       block verification is.

    2. numblocks (numeric, optional, default=288, 0=all) The number of
       blocks to check.

  Result:
    true|false      (boolean) Verified or not."
  [])


(defrpc verifymessage
  "verifymessage \"dogecoinaddress\" \"signature\" \"message\"

  Verify a signed message

  Arguments:

    1. \"dogecoinaddress\" (string, required) The dogecoin address to
       use for the signature.

    2. \"signature\" (string, required) The signature provided by the
       signer in base 64 encoding (see signmessage).

    3. \"message\" (string, required) The message that was signed.

  Result:
    true|false   (boolean) If the signature is verified or not."
  [dogecoinaddress signature message]
  {:pre [(string? dogecoinaddress)
         (string? signature)
         (string? message)]})
