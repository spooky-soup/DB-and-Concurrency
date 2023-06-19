# -*- coding: utf-8 -*-
import psycopg2
from dotenv import load_dotenv
from flask import Flask, request, jsonify
import json

app = Flask(__name__)
url = "http://localhost:5432="
conn = psycopg2.connect(host='localhost', port=5432, dbname='demo', user='spooky', password='admin')


@app.route('/')
def index():
    return "Hello, World!"


def get_request(request: str):
    with conn:
        with conn.cursor() as cursor:
            cursor.execute(request)
            res = cursor.fetchall()
            if res:
                return json.dumps(res, indent=4, sort_keys=True, default=str)
            else:
                return jsonify({"error": f" not found."}), 404


# ---------------1-----------------
# List all the available source and destination cities
GET_CITIES = "SELECT DISTINCT city FROM airports"


@app.route("/cities", methods=["GET"])
def get_all_cities():
    return get_request(GET_CITIES)


# ---------------2-----------------
# List all the available source and destination airports
GET_AIRPORTS = "SELECT airport_code, airport_name FROM airports"


@app.route("/airports", methods=["GET"])
def get_all_airports():
    return get_request(GET_AIRPORTS)


# ---------------3-----------------
# List the airports within a city
def get_airports_within_city_query(city: str):
    return 'SELECT airport_code, airport_name FROM airports_data where city @> \'{{\"ru\": \"{0}\"}}\' OR city @> \'{{\"en\": \"{0}\"}}\' ORDER BY airport_code ASC'.format(city)


@app.route("/airports/<string:city>", methods=["GET"])
def get_airports(city: str):
    req = get_airports_within_city_query(city)
    return get_request(req)


# ---------------4-----------------
# List the inbound schedule for an airport
def get_inbound_schedule_query(airport_code: str):
    return """SELECT flight_no, EXTRACT(ISODOW FROM scheduled_arrival) as day_of_week,
                scheduled_arrival::time as arrival_time, departure_airport
        FROM flights
        GROUP BY flight_no, day_of_week, arrival_time, departure_airport, arrival_airport
        HAVING arrival_airport = \'{}\'
        ORDER BY day_of_week;""".format(airport_code)

@app.route("/inbound-schedule/<string:airport>", methods=["GET"])
def get_inbound_schedule(airport: str):
    return get_request(get_inbound_schedule_query(airport))


# ---------------5-----------------
# List  the outbound schedule for an airport
def get_outbound_schedule_query(airport_code: str):
    return """SELECT flight_no, EXTRACT(ISODOW FROM scheduled_arrival) as day_of_week,
                scheduled_departure::time as departure_time, arrival_airport
        FROM flights
        GROUP BY flight_no, day_of_week, departure_time, departure_airport, arrival_airport
        HAVING departure_airport = \'{}\'
        ORDER BY day_of_week;""".format(airport_code)

@app.route("/outbound-schedule/<string:airport>", methods=["GET"])
def get_outbound_schedule(airport: str):
    return get_request(get_outbound_schedule_query(airport))

if __name__ == '__main__':
    app.run(debug=True)
