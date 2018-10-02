from sqlalchemy import Column, ForeignKey, Integer, String, Boolean, Enum, DateTime, Float
from sqlalchemy.orm import relationship
from marshmallow import Schema, fields, post_load
import enum
import sys
import datetime
sys.path.insert(0, '../../')
from db.base import Base

class Table(Base):
	__tablename__ = 'tables'
	id = Column(Integer, primary_key=True)
	table_number = Column(Integer)
	seats = Column(Integer)
	qr_code = Column(String)
	passcode = Column(String)
	status = Column(Boolean)
	order = relationship("Order", uselist=False)

	def __init__(self, table_number, seats, qr_code, passcode, status):
		self.table_number = table_number
		self.seats = seats
		self.qr_code = qr_code
		self.passcode = passcode
		self.status = status